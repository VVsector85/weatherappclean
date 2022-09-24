package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.LocalRepository

class SaveRequestListUseCase (private val localRepository:LocalRepository){

   fun execute(saveWeatherCardsList: List<WeatherCard>):Boolean {
       return localRepository.saveWeatherCards(saveWeatherCardsList =  saveWeatherCardsList)
   }
}