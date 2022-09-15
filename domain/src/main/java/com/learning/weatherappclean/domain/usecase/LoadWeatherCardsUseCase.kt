package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.LocalRepository

class LoadWeatherCardsUseCase(private val localRepository: LocalRepository) {

     fun execute ():List<WeatherCard>{

        return localRepository.loadWeatherCards()
    }

}