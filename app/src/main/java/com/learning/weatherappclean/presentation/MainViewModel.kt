package com.learning.weatherappclean.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.usecase.GetWeatherCardDataUseCase
import com.learning.weatherappclean.domain.usecase.SaveWeatherCardsUseCase
import com.learning.weatherappclean.domain.usecase.LoadWeatherCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (private val loadWeatherCardsUseCase : LoadWeatherCardsUseCase,
                                         private val saveWeatherCardsUseCase : SaveWeatherCardsUseCase,
                                         private val getWeatherCardDataUseCase: GetWeatherCardDataUseCase):ViewModel(){


    private val resultLiveMutable = MutableLiveData<String>()

    fun getResult(): LiveData<String> {
        return resultLiveMutable
    }
    fun save(text:String){
        resultLiveMutable.value = saveWeatherCardsUseCase.execute(listOf(WeatherCard(location = text))).toString()
    }
    fun load (){
        val weatherCards :List <WeatherCard> = loadWeatherCardsUseCase.execute()
        resultLiveMutable.value = weatherCards.toString()
    }
    fun getWeather(){
        viewModelScope.launch(IO) {
          try{
              Log.d("my_tag","try")
              resultLiveMutable.postValue( getWeatherCardDataUseCase.execute(WeatherCard("Paris")).toString())
          }catch (ex: Exception){
              Log.d("my_tag",ex.toString())
          }

        }
    }


init {
    Log.d("my_tag", "View model created")
}
}