package com.learning.weatherappclean.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadRequestListUseCase: LoadRequestListUseCase,
    private val saveRequestListUseCase: SaveRequestListUseCase,
    private val getWeatherCardDataUseCase: GetWeatherCardDataUseCase,
    private val getAutocompletePredictionsUseCase: GetAutocompletePredictionsUseCase,
    private val loadSettingsUseCase: LoadSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : ViewModel() {
    private val loadingState = MutableStateFlow(true)
    private val weatherCardsList = MutableStateFlow(emptyList<WeatherCard>().toMutableList())
    private val prediction = MutableStateFlow(emptyList<AutocompletePrediction.Predictions>())
    private val scrollToFirst = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow("")
    private val settings = MutableStateFlow(Settings())

    init {
        settings.value = loadSettingsUseCase.execute()
        refreshCards()
    }

    val getError: StateFlow<String> get() = errorMessage.asStateFlow()
    val getScrollToFirst: StateFlow<Boolean> get() = scrollToFirst.asStateFlow()
    val getLoadingState: StateFlow<Boolean> get() = loadingState.asStateFlow()
    val getPredictions: StateFlow<List<AutocompletePrediction.Predictions>> get() = prediction.asStateFlow()
    val getList: StateFlow<List<WeatherCard>>get() = weatherCardsList.asStateFlow()
    val getSettings: StateFlow<Settings> get()= settings.asStateFlow()


    fun deleteCard(index: Int) {
        val list = weatherCardsList.value.toMutableList()
        list.removeAt(index)
        scrollToFirst.value = (false)
        weatherCardsList.value = list
        saveRequestListUseCase.execute(list)
    }




        fun saveSettings(){
            settings.value.fahrenheit = true
            saveSettingsUseCase.execute(settings.value)
        }





    fun addCard(location: String) {

        loadingState.value = true
        viewModelScope.launch(IO) {
            val card = getWeatherCardDataUseCase.execute(Request(location))
            if (!card.error) {
                val list = weatherCardsList.value.toMutableList()
                if (list.find { it.location == card.location && it.country == card.country } == null) {
                    list.add(card)
                        //weatherCardsList.value = list
                    weatherCardsList.emit(list)
                    saveRequestListUseCase.execute(list)
                } else {
                    errorMessage.value =
                        ("${card.location}, ${card.country} is already on the list")
                }

            } else {
                errorMessage.value = card.errorMsg
                loadingState.value = false
                delay(10000)
                errorMessage.value = ""
            }
            loadingState.value = false
            scrollToFirst.value = true

        }
    }

    fun retrievePredictions(text: String) {
        if (text.length < 3) {
            prediction.value = emptyList()
            return
        }
        viewModelScope.launch(IO) {
            val predictionData =
                getAutocompletePredictionsUseCase.execute(Request(text))
            if (!predictionData.error) {
                prediction.value = predictionData.predictions
            } else {
                loadingState.value = false
            }
        }
    }

    fun refreshCards() {
        loadingState.value = true
        val emptyCards = loadRequestListUseCase.execute()
        val filledList = mutableListOf<WeatherCard>()
        viewModelScope.launch(IO) {
            emptyCards.forEach {
                it.units = "m"
                val t = getWeatherCardDataUseCase.execute(it)
                if (!t.error) {
                    filledList.add(t)
                } else {
                    errorMessage.value = t.errorMsg
                    loadingState.value = false
                    delay(10000)
                    errorMessage.value = ""
                }
            }
                //weatherCardsList.value = filledList
            weatherCardsList.emit(filledList)
            loadingState.value = false
            scrollToFirst.value = true
        }
    }

    fun swapSections(a: Int, b: Int) {
        val list = weatherCardsList.value.reversed().toMutableList()
        val x = list[a]
        list[a] = list[b]
        list[b] = x
        weatherCardsList.value = list.asReversed()
        saveRequestListUseCase.execute(weatherCardsList.value)
    }
}