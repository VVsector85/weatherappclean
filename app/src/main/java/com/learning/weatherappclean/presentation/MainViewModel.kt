package com.learning.weatherappclean.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.weatherappclean.R


import com.learning.weatherappclean.domain.model.Autocomplete
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.reflect.KProperty1

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadRequestListUseCase: LoadRequestListUseCase,
    private val saveRequestListUseCase: SaveRequestListUseCase,
    private val getWeatherCardDataUseCase: GetWeatherCardDataUseCase,
    private val getAutocompletePredictionsUseCase: GetAutocompletePredictionsUseCase,
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : ViewModel() {
    private val isLoading = MutableStateFlow(true)
    private val weatherCardsList = MutableStateFlow(emptyList<WeatherCard>())


    private val scrollToFirst = MutableStateFlow(Pair(false, 0))
    private val errorMessage = MutableStateFlow("")
    private val settings = MutableStateFlow(Settings())
    private val noRequests = MutableStateFlow(false)
    private val expanded = MutableStateFlow(false)
    private val showSearch = MutableStateFlow(false)
    private val searchText = MutableStateFlow("")

    init {
        settings.value = loadSettingsUseCase.execute()
        refreshCards()
    }

    val getError: StateFlow<String> get() = errorMessage.asStateFlow()
    val getScrollToFirst: StateFlow<Pair<Boolean, Int>> get() = scrollToFirst.asStateFlow()
    val getLoadingState: StateFlow<Boolean> get() = isLoading.asStateFlow()
    val getPredictions: Flow<List<Autocomplete.Predictions>> get() = predictions
    val getList: StateFlow<List<WeatherCard>> get() = weatherCardsList.asStateFlow()
    val getSettings: StateFlow<Settings> get() = settings.asStateFlow()
    val getNoRequests: StateFlow<Boolean> get() = noRequests.asStateFlow()
    val getExpanded: StateFlow<Boolean> get() = expanded.asStateFlow()
    val getShowSearch: StateFlow<Boolean> get() = showSearch.asStateFlow()
    val getSearchText: MutableStateFlow<String> get() = searchText


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val predictions = searchText.debounce(AUTOCOMPLETE_QUERY_DELAY)
        .distinctUntilChanged()
        .flatMapLatest { autocompleteQuery(it) }

    private suspend fun autocompleteQuery(query: String): Flow<List<Autocomplete.Predictions>> {
        if (query.length < AUTOCOMPLETE_QUERY_MIN_CHARS) {
            return flowOf(emptyList())
        }
        expanded.value = true
        return flowOf(getAutocompletePredictionsUseCase.execute(Request(query)).predictions)
    }

    fun deleteCard(index: Int) {
        val list = weatherCardsList.value.toMutableList()
        list.removeAt(index)
        if (list.isEmpty()) noRequests.value = true
        weatherCardsList.update { list }
        saveRequestListUseCase.execute(list)
    }


    fun saveSettings(value: Boolean, field: KProperty1<Settings, *>) {
        when (field) {
            Settings::fahrenheit -> settings.update { it.copy(fahrenheit = value) }
            Settings::newCardFirst -> settings.update { it.copy(newCardFirst = value) }
            Settings::dragAndDropCards -> settings.update { it.copy(dragAndDropCards = value) }
            Settings::detailsOnDoubleTap -> settings.update { it.copy(detailsOnDoubleTap = value) }
        }

        saveSettingsUseCase.execute(settings.value)
    }


    fun addCard(location: String, prediction: Autocomplete.Predictions?) {
        expanded.value = false
        isLoading.value = true
        viewModelScope.launch(IO) {
            val card = getWeatherCardDataUseCase.execute(
                Request(
                    query = location,
                    units = if (settings.value.fahrenheit) "f" else "m",
                    lat = prediction?.lat ?: "",
                    lon = prediction?.lon?:"",
                    location = prediction?.location?:"",
                    country = prediction?.country?:"",
                    region = prediction?.region?:"",
                )
            )
            if (!card.error) {
                val list = weatherCardsList.value.toMutableList()
                if (list.find { it.location == card.location && it.country == card.country && it.region == card.region } == null) {
                    if (settings.value.newCardFirst) list.add(0, card) else list.add(card)
                    noRequests.emit(false)
                    weatherCardsList.emit(list)
                    scrollToFirst.value = Pair(
                        true,
                        if (settings.value.newCardFirst) 0 else weatherCardsList.value.size
                    )
                    searchText.value = ""
                    saveRequestListUseCase.execute(list)
                    showSearch.emit(false)
                } else {
                    // TODO:  
                    errorMessage.value =
                        "${card.location}, ${card.country}, ${card.region}" + R.string.isOnTheList
                    isLoading.value = false
                    delay(ERROR_MESSAGE_DELAY)
                    errorMessage.value = ""
                }
            } else {

                errorMessage.value = card.errorMsg
                isLoading.value = false
                delay(ERROR_MESSAGE_DELAY)
                errorMessage.value = ""
            }
            isLoading.value = false


        }
    }

    fun refreshCards() {
        isLoading.value = true
        val requestList = loadRequestListUseCase.execute()
        noRequests.tryEmit(requestList.isEmpty())
        val tempCardList = mutableListOf<WeatherCard>()
        viewModelScope.launch(IO) {

            run breaking@{
                requestList.forEach {
                    it.units = if (settings.value.fahrenheit) "f" else "m"

                    val t = getWeatherCardDataUseCase.execute(it)
                    if (!t.error) {
                        tempCardList.add(t)

                    } else {

                        errorMessage.value = t.errorMsg
                        isLoading.value = false
                        delay(ERROR_MESSAGE_DELAY)
                        errorMessage.value = ""
                        return@breaking false
                    }
                }
            }
            weatherCardsList.emit(tempCardList)
            scrollToFirst.value =
                Pair(true, if (settings.value.newCardFirst) 0 else weatherCardsList.value.size)
            isLoading.value = false
        }
    }

    fun setShowDetails(boolean: Boolean, index: Int) {
        val list = weatherCardsList.value
        list[index].showDetails = boolean
        weatherCardsList.value = list
        saveRequestListUseCase.execute(list)
    }

    fun setShowSearch(boolean: Boolean) {
        showSearch.update { boolean }
    }

    fun setExpanded(boolean: Boolean) {
        expanded.update { boolean }
    }

    fun swapSections(a: Int, b: Int) {
        val list = weatherCardsList.value.toMutableList()
        Collections.swap(list, a, b)
        weatherCardsList.tryEmit(list)
        saveRequestListUseCase.execute(weatherCardsList.value)
    }

    fun stopScrollToFirst() {
        scrollToFirst.value = Pair(false, 0)
    }

    companion object {
        private const val ERROR_MESSAGE_DELAY = 6000L
        private const val AUTOCOMPLETE_QUERY_DELAY = 1500L
        private const val AUTOCOMPLETE_QUERY_MIN_CHARS = 3
    }
}