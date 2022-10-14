package com.learning.weatherappclean.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.weatherappclean.util.ErrorMapper
import com.learning.weatherappclean.util.ErrorMessage
import com.learning.weatherappclean.util.ErrorTypeUi
import com.learning.weatherappclean.data.model.ErrorType
import com.learning.weatherappclean.domain.model.*
import com.learning.weatherappclean.domain.usecase.*
import com.learning.weatherappclean.util.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
    private val saveSettingsUseCase: SaveSettingsUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    loadSettingsUseCase: LoadSettingsUseCase
) : ViewModel() {
    private val isLoading = MutableStateFlow(true)
    private val weatherCardsList = MutableStateFlow(emptyList<WeatherCard>())
    private val scrollToFirst = MutableStateFlow(Pair(false, 0))
    private val errorMessage = MutableStateFlow(ErrorMessage())
    private val settings = MutableStateFlow(Settings())
    private val noRequests = MutableStateFlow(false)
    private val expanded = MutableStateFlow(false)
    private val showSearch = MutableStateFlow(false)
    private val searchText = MutableStateFlow("")
    private var lastRefreshTime = System.currentTimeMillis()

    init {
        settings.value = loadSettingsUseCase()
        refreshCards()
    }

    val getError: StateFlow<ErrorMessage> get() = errorMessage.asStateFlow()
    val getScrollToFirst: StateFlow<Pair<Boolean, Int>> get() = scrollToFirst.asStateFlow()
    val getLoadingState: StateFlow<Boolean> get() = isLoading.asStateFlow()
    val getPredictions: Flow<List<AutocompletePrediction>> get() = predictions
    val getCardList: StateFlow<List<WeatherCard>> get() = weatherCardsList.asStateFlow()
    val getSettings: StateFlow<Settings> get() = settings.asStateFlow()
    val getNoRequests: StateFlow<Boolean> get() = noRequests.asStateFlow()
    val getExpanded: StateFlow<Boolean> get() = expanded.asStateFlow()
    val getShowSearch: StateFlow<Boolean> get() = showSearch.asStateFlow()
    val getSearchText: MutableStateFlow<String> get() = searchText


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val predictions = searchText.debounce(AUTOCOMPLETE_QUERY_DELAY)
        .distinctUntilChanged()
        .flatMapLatest { autocompleteQuery(query = it) }

    private suspend fun autocompleteQuery(query: String): Flow<List<AutocompletePrediction>> {
        if (query.length < AUTOCOMPLETE_QUERY_MIN_CHARS) {
            return flowOf(emptyList())
        }
        expanded.emit(true)
        return flowOf(getAutocompletePredictionsUseCase(Request(query)).data!!)
    }

    fun deleteCard(index: Int) {
        val tempCardList = weatherCardsList.value.toMutableList()
        tempCardList.removeAt(index)
        if (tempCardList.isEmpty()) noRequests.value = true
        weatherCardsList.value = tempCardList
        saveRequestListUseCase(tempCardList)
    }

    fun saveSettings(value: Boolean, field: KProperty1<Settings, *>) {
        when (field) {
            Settings::imperialUnits -> settings.update { it.copy(imperialUnits = value) }
            Settings::newCardFirst -> settings.update { it.copy(newCardFirst = value) }
            Settings::dragAndDropCards -> settings.update { it.copy(dragAndDropCards = value) }
            Settings::detailsOnDoubleTap -> settings.update { it.copy(detailsOnDoubleTap = value) }
        }
        saveSettingsUseCase(settings.value)
    }

    fun addCard(location: String, prediction: AutocompletePrediction?) {
        expanded.value = false
        isLoading.value = true
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            val resourceDomain = getWeatherCardDataUseCase(
                Request(
                    query = location,
                    units = if (settings.value.imperialUnits) "f" else "m",
                    lat = prediction?.lat ?: "",
                    lon = prediction?.lon ?: "",
                    location = prediction?.location ?: "",
                    country = prediction?.country ?: "",
                    region = prediction?.region ?: "",
                )
            )
            if (resourceDomain is ResourceDomain.Success) {
                val card = resourceDomain.data!!
                val tempCardList = weatherCardsList.value.toMutableList()
                if (tempCardList.find { it.location == card.location && it.country == card.country && it.region == card.region } == null) {
                    if (settings.value.newCardFirst) tempCardList.add(
                        0,
                        resourceDomain.data!!
                    ) else tempCardList.add(resourceDomain.data!!)
                    noRequests.emit(false)
                    weatherCardsList.emit(tempCardList)
                    scrollToFirst.emit (Pair(
                        true,
                        if (settings.value.newCardFirst) 0 else weatherCardsList.value.size
                    ))
                    searchText.emit("")
                    saveRequestListUseCase(tempCardList)
                    showSearch.emit(false)
                } else {
                    errorMessage.emit (ErrorMessage(
                        errorType = ErrorTypeUi.SAME_ITEM_ERROR,
                        showTime = DEFAULT_ERROR_MESSAGE_SHOW_TIME,
                        errorCode = null,
                        errorString = "${card.location}, ${card.country}, ${card.region}"
                    ))
                    isLoading.emit(false)
                }
            } else {
                errorMessage.emit(ErrorMessage(
                    errorType = ErrorMapper().mapToPresentation(resourceDomain.type as ErrorType),
                    showTime = DEFAULT_ERROR_MESSAGE_SHOW_TIME,
                    errorCode = resourceDomain.code,
                    errorString = resourceDomain.message?:""
                ))
                isLoading.emit(false)
            }
            isLoading.emit(false)
        }
    }

    fun refreshCards() {
        isLoading.value = true
        var duplicatesFound = false
        val requestList = loadRequestListUseCase()
        noRequests.tryEmit(requestList.isEmpty())
        val tempCardList = mutableListOf<WeatherCard>()
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            run breaking@{
                requestList.forEach { request ->
                    request.units = if (settings.value.imperialUnits) "f" else "m"
                    val resourceDomain = getWeatherCardDataUseCase(request)
                    if (resourceDomain is ResourceDomain.Success) {
                        val card = resourceDomain.data!!
                        if (tempCardList.find { it.location == card.location && it.country == card.country && it.region == card.region } == null) {
                            tempCardList.add(card)
                        } else {
                            duplicatesFound = true
                        }
                    } else {
                        errorMessage.emit(ErrorMessage(
                            errorType = ErrorMapper().mapToPresentation(resourceDomain.type as ErrorType),
                            showTime = DEFAULT_ERROR_MESSAGE_SHOW_TIME,
                            errorCode = resourceDomain.code,
                            errorString = resourceDomain.message?:""
                        ))
                        isLoading.emit(false)
                        return@breaking
                    }
                }
            }
            weatherCardsList.emit(tempCardList)
            lastRefreshTime = System.currentTimeMillis()
            scrollToFirst.emit(Pair(true, if (settings.value.newCardFirst) 0 else weatherCardsList.value.size))
            isLoading.emit(false)
            if (duplicatesFound) saveRequestListUseCase(tempCardList)
        }
    }

    fun setShowDetails(boolean: Boolean, index: Int) {
        val list = weatherCardsList.value
        list[index].showDetails = boolean
        weatherCardsList.value = list
        saveRequestListUseCase(list)
    }


    fun swapSections(a: Int, b: Int) {
        val list = weatherCardsList.value.toMutableList()
        Collections.swap(list, a, b)
        weatherCardsList.value = list
        saveRequestListUseCase(weatherCardsList.value)
    }

    fun setShowSearch(value: Boolean) {
        showSearch.value = value
        searchText.value = ""
    }

    fun setExpanded(value: Boolean) {
        expanded.value = value
    }

    fun setSearchText(value: String) {
        searchText.value = value
    }

    fun stopScrollToFirst() {
        scrollToFirst.value = Pair(false, 0)
    }

    fun resetErrorMessage() {
        errorMessage.value = ErrorMessage()
    }

    fun refreshCardsOnResume() {
        if (System.currentTimeMillis() - lastRefreshTime > REFRESH_ON_RESUME_TIMEOUT) refreshCards()
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE_SHOW_TIME = 8000L
        private const val AUTOCOMPLETE_QUERY_DELAY = 1500L
        private const val AUTOCOMPLETE_QUERY_MIN_CHARS = 3
        private const val REFRESH_ON_RESUME_TIMEOUT = 1800000L
    }
}