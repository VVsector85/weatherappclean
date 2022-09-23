package com.learning.weatherappclean.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.usecase.GetAutocompletePredictionsUseCase
import com.learning.weatherappclean.domain.usecase.GetWeatherCardDataUseCase
import com.learning.weatherappclean.domain.usecase.SaveWeatherCardsUseCase
import com.learning.weatherappclean.domain.usecase.LoadWeatherCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadWeatherCardsUseCase: LoadWeatherCardsUseCase,
    private val saveWeatherCardsUseCase: SaveWeatherCardsUseCase,
    private val getWeatherCardDataUseCase: GetWeatherCardDataUseCase,
    private val getAutocompletePredictionsUseCase: GetAutocompletePredictionsUseCase
) : ViewModel() {


    private val loadingState = MutableStateFlow(true)
    private val weatherCardsMutableList = MutableStateFlow(emptyList<WeatherCard>())
    private val prediction = MutableStateFlow(emptyList<AutocompletePrediction.Predictions>())

    private val scrollToFirst = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow("")
    init {
        refreshCards()
    }



    val getError: StateFlow<String> get() = errorMessage.asStateFlow()
    val getScrollToFirst: StateFlow<Boolean> get()= scrollToFirst.asStateFlow()
    val getList: StateFlow<List<WeatherCard>> get()= weatherCardsMutableList.asStateFlow()
    val getLoadingState: StateFlow<Boolean> get()= loadingState.asStateFlow()
    val getPredictions: StateFlow<List<AutocompletePrediction.Predictions>> get()= prediction.asStateFlow()


    fun deleteCard(index: Int) {
        val list = weatherCardsMutableList.value.toMutableList()
        list.removeAt(index)
        list.forEachIndexed { ind, it -> it.number = ind }
        scrollToFirst.value = (false)
        weatherCardsMutableList.value = list.toList()
        saveWeatherCardsUseCase.execute(list.toList())
    }

    fun addCard(location: String) {
        var card = WeatherCard("")
        loadingState.value = true
        viewModelScope.launch(IO) {
            try {
                card = getWeatherCardDataUseCase.execute(Request(location))
            } catch (e: Exception) {
                Log.d("my_tag", e.toString())
            }
            if (!card.error) {
                val list = weatherCardsMutableList.value.toMutableList()

                if (list.find { it.location == card.location&&it.country == card.country  } == null) {
                    list.add(card)
                    list.forEachIndexed { index, it -> it.number = index }
                    weatherCardsMutableList.value = list
                    saveWeatherCardsUseCase.execute(list)
                } else {
                    errorMessage.value =("${card.location}, ${card.country} is already on the list")
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

            Log.d("my_tag", "autocomplete query")
            val predictionData =
                getAutocompletePredictionsUseCase.execute(Request(text))
            if (!predictionData.error)
                prediction.value = predictionData.predictions
            Log.d("my_tag", prediction.value.toString())


        }
    }

    fun refreshCards() {
        loadingState.value = true
        val emptyCards = loadWeatherCardsUseCase.execute()
        val filledList = mutableListOf<WeatherCard>()
        viewModelScope.launch(IO) {


            Log.d("my_tag", "refresh cards query")
            emptyCards.forEach {

                val t = getWeatherCardDataUseCase.execute(Request(it.location))
                if (!t.error) {
                    filledList.add(t)
                } else {
                    Log.d("my_tag", t.errorMsg)
                }
            }

            weatherCardsMutableList.value = filledList
            loadingState.value = false
            scrollToFirst.value = true

        }
    }


}