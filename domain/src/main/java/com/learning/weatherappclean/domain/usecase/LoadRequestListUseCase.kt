package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.WeatherRequest
import com.learning.weatherappclean.domain.repository.RequestsRepository

class LoadRequestListUseCase(private val requestsRepository: RequestsRepository) {
    operator fun invoke(): List<WeatherRequest> = requestsRepository.loadWeatherCards()
}
