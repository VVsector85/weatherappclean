package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RequestsRepository

class SaveRequestListUseCase (private val requestsRepository:RequestsRepository){

   fun execute(saveWeatherCardsList: List<WeatherCard>):Boolean {
       return requestsRepository.saveWeatherCards(saveWeatherCardsList =  saveWeatherCardsList)
   }
}