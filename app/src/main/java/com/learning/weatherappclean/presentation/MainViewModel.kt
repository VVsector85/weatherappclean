package com.learning.weatherappclean.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.weatherappclean.data.model.ErrorType
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.AutocompleteRequest
import com.learning.weatherappclean.domain.model.ResourceDomain
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.model.WeatherRequest
import com.learning.weatherappclean.domain.usecase.GetAutocompletePredictionsUseCase
import com.learning.weatherappclean.domain.usecase.GetWeatherCardDataUseCase
import com.learning.weatherappclean.domain.usecase.LoadRequestListUseCase
import com.learning.weatherappclean.domain.usecase.LoadSettingsUseCase
import com.learning.weatherappclean.domain.usecase.SaveRequestListUseCase
import com.learning.weatherappclean.domain.usecase.SaveSettingsUseCase
import com.learning.weatherappclean.util.Constants.IMPERIAL_UNITS
import com.learning.weatherappclean.util.Constants.METRIC_UNITS
import com.learning.weatherappclean.util.CoroutineDispatcherProvider
import com.learning.weatherappclean.util.ErrorMapper
import com.learning.weatherappclean.util.ErrorMessageProvider
import com.learning.weatherappclean.util.ErrorTypeUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections
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
    private val errorMessage = MutableStateFlow(ErrorMessageProvider())
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

    val getError: StateFlow<ErrorMessageProvider> get() = errorMessage.asStateFlow()
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

    fun addCard(location: String, prediction: AutocompletePrediction?) {
        expanded.value = false
        isLoading.value = true
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            val resourceDomain = getWeatherCardDataUseCase(
                WeatherRequest(
                    query = location,
                    units = if (settings.value.imperialUnits) IMPERIAL_UNITS else METRIC_UNITS,
                    lat = prediction?.lat,
                    lon = prediction?.lon,
                    location = prediction?.location,
                    country = prediction?.country,
                    region = prediction?.region,
                )
            )
            when (resourceDomain) {
                is ResourceDomain.Success -> {
                    val card = resourceDomain.data
                    val tempCardList = weatherCardsList.value.toMutableList()
                    if (tempCardList.find { it.location == card.location && it.country == card.country && it.region == card.region } == null) {
                        if (settings.value.newCardFirst) tempCardList.add(
                            index = 0,
                            element = resourceDomain.data
                        ) else tempCardList.add(resourceDomain.data)
                        noRequests.emit(false)
                        weatherCardsList.emit(tempCardList)
                        scrollToFirst.emit(
                            Pair(
                                true,
                                if (settings.value.newCardFirst) 0 else weatherCardsList.value.size
                            )
                        )
                        searchText.emit("")
                        saveRequestListUseCase(tempCardList)
                        showSearch.emit(false)
                    } else {
                        errorMessage.emit(
                            ErrorMessageProvider(
                                errorType = ErrorTypeUi.SAME_ITEM_ERROR,
                                showTime = DEFAULT_ERROR_MESSAGE_SHOW_TIME,
                                errorCode = null,
                                errorString = "${card.location}, ${card.country}, ${card.region}"
                            )
                        )
                        isLoading.emit(false)
                    }
                }
                is ResourceDomain.Error -> {
                    errorMessage.emit(
                        ErrorMessageProvider(
                            errorType = ErrorMapper().mapToPresentation(resourceDomain.type as ErrorType),
                            showTime = DEFAULT_ERROR_MESSAGE_SHOW_TIME,
                            errorCode = resourceDomain.code,
                            errorString = resourceDomain.message
                        )
                    )
                    isLoading.emit(false)
                }
            }
            isLoading.emit(false)
        }
    }

    fun refreshCards() {
        isLoading.value = true
        var duplicatesFound = false
        val requestList = loadRequestListUseCase()
        noRequests.value = requestList.isEmpty()
        val tempCardList = mutableListOf<WeatherCard>()
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            run breaking@{
                getWeatherResources(
                    weatherRequestList = requestList,
                    units = if (settings.value.imperialUnits) IMPERIAL_UNITS else METRIC_UNITS
                ).forEach { resourceDomain ->

                    when (resourceDomain) {
                        is ResourceDomain.Success -> {
                            val card = resourceDomain.data
                            if (tempCardList.find { it.location == card.location && it.country == card.country && it.region == card.region } == null) {
                                tempCardList.add(card)
                            } else {
                                duplicatesFound = true
                            }
                        }
                        is ResourceDomain.Error -> {
                            errorMessage.emit(
                                ErrorMessageProvider(
                                    errorType = ErrorMapper().mapToPresentation(resourceDomain.type as ErrorType),
                                    showTime = DEFAULT_ERROR_MESSAGE_SHOW_TIME,
                                    errorCode = resourceDomain.code,
                                    errorString = resourceDomain.message
                                )
                            )
                            isLoading.emit(false)
                            return@breaking
                        }
                    }
                }
            }
            weatherCardsList.emit(tempCardList)
            lastRefreshTime = System.currentTimeMillis()
            scrollToFirst.emit(
                Pair(
                    true,
                    if (settings.value.newCardFirst) 0 else weatherCardsList.value.size
                )
            )
            isLoading.emit(false)
            if (duplicatesFound) saveRequestListUseCase(tempCardList)
        }
    }

    fun deleteCard(index: Int) {
        val tempCardList = weatherCardsList.value.toMutableList()
        tempCardList.removeAt(index)
        if (tempCardList.isEmpty()) noRequests.value = true
        weatherCardsList.value = tempCardList
        saveRequestListUseCase(tempCardList)
    }

    fun saveSettings(value: Boolean, property: KProperty1<Settings, *>) {
        when (property) {
            Settings::imperialUnits -> settings.update { it.copy(imperialUnits = value) }
            Settings::newCardFirst -> settings.update { it.copy(newCardFirst = value) }
            Settings::dragAndDropCards -> settings.update { it.copy(dragAndDropCards = value) }
            Settings::detailsOnDoubleTap -> settings.update { it.copy(detailsOnDoubleTap = value) }
        }
        saveSettingsUseCase(settings.value)
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
        errorMessage.value = ErrorMessageProvider()
    }

    fun refreshCardsOnResume() {
        if (System.currentTimeMillis() - lastRefreshTime > REFRESH_ON_RESUME_TIMEOUT) refreshCards()
    }

    private suspend fun autocompleteQuery(query: String): Flow<List<AutocompletePrediction>> {
        if (query.length < AUTOCOMPLETE_QUERY_MIN_CHARS) {
            return flowOf(emptyList())
        }
        if (showSearch.value) expanded.emit(true)
        val result = getAutocompletePredictionsUseCase(AutocompleteRequest(query))
        return if (result is ResourceDomain.Success)
            flowOf(result.data) else flowOf(emptyList())
    }

    private suspend fun getWeatherResources(
        weatherRequestList: List<WeatherRequest>,
        units: String
    ): List<ResourceDomain<WeatherCard>> {
        val resourceList = mutableListOf<Deferred<ResourceDomain<WeatherCard>>>()
        coroutineScope {
            weatherRequestList.forEach { request ->
                request.units = units
                resourceList.add(
                    async {
                        getWeatherCardDataUseCase(request)
                    }
                )
            }
        }
        return resourceList.awaitAll()
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE_SHOW_TIME = 8000L
        private const val AUTOCOMPLETE_QUERY_DELAY = 1500L
        private const val AUTOCOMPLETE_QUERY_MIN_CHARS = 3
        private const val REFRESH_ON_RESUME_TIMEOUT = 1800000L
    }
}
