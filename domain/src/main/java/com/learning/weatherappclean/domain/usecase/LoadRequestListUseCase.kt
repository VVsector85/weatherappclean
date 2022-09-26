package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.LocalRepository

class LoadRequestListUseCase(private val localRepository: LocalRepository) {

     fun execute ():List<Request> = localRepository.loadWeatherCards()


}