package com.learning.weatherappclean.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.usecase.SaveWeatherCardsUseCase
import com.learning.weatherappclean.domain.usecase.LoadWeatherCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (private val loadWeatherCardsUseCase : LoadWeatherCardsUseCase,
                                         private val saveWeatherCardsUseCase : SaveWeatherCardsUseCase):ViewModel(){


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


init {
    Log.d("tag", "View model created")
}
}