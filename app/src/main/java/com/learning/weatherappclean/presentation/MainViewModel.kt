package com.learning.weatherappclean.presentation


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.weatherappclean.data.model.apierror.connection.ErrorType

import com.learning.weatherappclean.domain.model.AutocompletePrediction
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
        // private val prediction = MutableStateFlow(emptyList<AutocompletePrediction.Predictions>())
    private val scrollToFirst = MutableStateFlow(Pair(false, 0))
    private val errorMessage = MutableStateFlow("")
    private val settings = MutableStateFlow(Settings())
    private val noRequests = MutableStateFlow(false)
    private val expanded = MutableStateFlow(false)

    private val searchText = MutableStateFlow("")

    init {
        settings.value = loadSettingsUseCase.execute()
        refreshCards()
    }

    val getError: StateFlow<String> get() = errorMessage.asStateFlow()
    val getScrollToFirst: StateFlow<Pair<Boolean, Int>> get() = scrollToFirst.asStateFlow()
    val getLoadingState: StateFlow<Boolean> get() = isLoading.asStateFlow()
    val getPredictions: Flow<List<AutocompletePrediction.Predictions>> get() = predictions
    val getList: StateFlow<List<WeatherCard>> get() = weatherCardsList.asStateFlow()
    val getSettings: StateFlow<Settings> get() = settings.asStateFlow()
    val getNoRequests: StateFlow<Boolean> get() = noRequests.asStateFlow()
    val getExpanded: MutableStateFlow<Boolean> get() = expanded
    val getSearchText: MutableStateFlow<String> get() = searchText


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val predictions = searchText.debounce(AUTOCOMPLETE_QUERY_DELAY)
        .distinctUntilChanged()
        .flatMapLatest {
            autocompleteQuery(it)
        }

    private suspend fun autocompleteQuery(query: String): Flow<List<AutocompletePrediction.Predictions>> {
        if (query.length<3) {
            return flowOf(emptyList())
        }
        Log.i("my_tag", "Autocomplete query: $query")
        expanded.value = true
        return flowOf(getAutocompletePredictionsUseCase.execute(Request(query)).predictions)
    }

    fun deleteCard(index: Int) {
        val list = weatherCardsList.value.toMutableList()
        list.removeAt(index)
        if (list.isEmpty())noRequests.value=true
        weatherCardsList.tryEmit(list)
        saveRequestListUseCase.execute(list)
    }



    fun saveSettings(value: Boolean, field: KProperty1<Settings, *>) {
        val t = settings.value
        when (field) {
            Settings::fahrenheit -> t.fahrenheit = value
            Settings::newCardFirst -> t.newCardFirst = value
            Settings::showCountry -> t.showCountry = value
            Settings::showFeelsLike -> t.showFeelsLike = value
        }
        settings.update { t.copy() }
        saveSettingsUseCase.execute(settings.value)
    }


    fun addCard(location: String) {
        expanded.value = false
        isLoading.value = true
        viewModelScope.launch(IO) {
            val card = getWeatherCardDataUseCase.execute(
                Request(request = location, units = if (settings.value.fahrenheit) "f" else "m")
            )
            if (!card.error) {
                val list = weatherCardsList.value.toMutableList()
                if (list.find { it.location == card.location && it.country == card.country } == null) {
                    if (settings.value.newCardFirst) list.add(0, card) else list.add(card)
                    noRequests.emit(false)
                    weatherCardsList.emit(list)
                    scrollToFirst.value = Pair(
                        true,
                        if (settings.value.newCardFirst) 0 else weatherCardsList.value.size
                    )
                    searchText.value = ""

                    saveRequestListUseCase.execute(list)
                } else {
                    errorMessage.value =
                        ("${card.location}, ${card.country} is already on the list")
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
            scrollToFirst.value = Pair(true, if (settings.value.newCardFirst) 0 else weatherCardsList.value.size)
            isLoading.value = false
        }
    }

    fun setShowDetails(bool: Boolean, index: Int) {
        val list = weatherCardsList.value
        list[index].showDetails = bool
        weatherCardsList.value = list
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

companion object{
    private const val ERROR_MESSAGE_DELAY = 6000L
    private const val AUTOCOMPLETE_QUERY_DELAY = 1500L
}
}