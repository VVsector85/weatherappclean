package com.learning.weatherappclean.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.usecase.GetAutocompletePredictionsUseCase
import com.learning.weatherappclean.domain.usecase.GetWeatherCardDataUseCase
import com.learning.weatherappclean.domain.usecase.SaveWeatherCardsUseCase
import com.learning.weatherappclean.domain.usecase.LoadWeatherCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val loadWeatherCardsUseCase : LoadWeatherCardsUseCase,
    private val saveWeatherCardsUseCase : SaveWeatherCardsUseCase,
    private val getWeatherCardDataUseCase: GetWeatherCardDataUseCase,
    private val getAutocompletePredictionsUseCase: GetAutocompletePredictionsUseCase
):ViewModel(){


    private val loadingState = MutableLiveData<Boolean>()
    private val weatherCardsMutableList = MutableLiveData<List<WeatherCard>>()
    private val prediction = MutableLiveData<List<String>>()
    private val errorMessage = MutableLiveData<String>()
    private val scrollToFirst = MutableLiveData<Int>()

    init {
            refreshCards()
    }


    fun getError(): LiveData<String> = errorMessage
    fun getList(): LiveData<List<WeatherCard>> = weatherCardsMutableList
    fun getLoadingState(): LiveData<Boolean> =  loadingState
    fun getScrollToFirst(): LiveData<Int> = scrollToFirst



    fun deleteCard(index:Int){
       val list =  weatherCardsMutableList.value?.toMutableList()?: emptyList<WeatherCard>().toMutableList()
        list.removeAt(index)
        list.forEachIndexed { ind, it -> it.number=ind }
        weatherCardsMutableList.value = list.toList()
        saveWeatherCardsUseCase.execute(list.toList())
    }

    fun addCard(location:String){
        var card = WeatherCard("")
        loadingState.value = true
        viewModelScope.launch(IO) {
           try {
               card = getWeatherCardDataUseCase.execute(WeatherCard(location))
           }catch (e:Exception){
               Log.d("my_tag",e.toString())
           }
            if (!card.error) {
                val list = weatherCardsMutableList.value?.toMutableList()
                    ?: emptyList<WeatherCard>().toMutableList()

              if (list.find { it.location == card.location}==null) {
                  list.add(card)


                  list.forEachIndexed { index, it -> it.number=index }
                  weatherCardsMutableList.postValue(list)
                  saveWeatherCardsUseCase.execute(list)
              }else{

                  errorMessage.postValue("${card.location} is already on the list")
              }

              } else {
                errorMessage.postValue(card.errorMsg)
              }

            loadingState.postValue(false)


            scrollToFirst.postValue(weatherCardsMutableList.value?.size?:1)

        }
    }





    fun getPredictions(text:String){
        if (text.length<3) return
        viewModelScope.launch(IO) {
            try{
                Log.d("my_tag","autocomplete query")
                val predictionData = getAutocompletePredictionsUseCase.execute(AutocompletePrediction(text))
                if (!predictionData.error)
                prediction.postValue(predictionData.predictions)
                Log.d("my_tag",prediction.value.toString())
            }catch (e: Exception){
                Log.d("my_tag", "Get predictions $e")
            }

        }
    }

 fun refreshCards(){
    loadingState.value = true
    val emptyCards = loadWeatherCardsUseCase.execute()
    val filledList = mutableListOf<WeatherCard>()
    viewModelScope.launch(IO) {

        try {
            Log.d("my_tag","refresh cards query")
            emptyCards.forEach {

               val t = getWeatherCardDataUseCase.execute(it)
               if (!t.error) { filledList.add(t) }else{Log.d("my_tag", t.errorMsg)}
            }
        }catch (e :Exception){
            Log.d("my_tag", "refresh cards:$e")
        }
        weatherCardsMutableList.postValue(filledList)
        loadingState.postValue(false)
       // if ((weatherCardsMutableList.value?.size?:0)>0){scrollToFirst.postValue(weatherCardsMutableList.value?.size?:1)}
        scrollToFirst.postValue(weatherCardsMutableList.value?.size?:1)

    }
}



}