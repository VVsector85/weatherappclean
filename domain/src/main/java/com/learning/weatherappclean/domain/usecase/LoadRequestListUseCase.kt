package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.repository.RequestsRepository

class LoadRequestListUseCase(private val requestsRepository: RequestsRepository) {

     fun execute ():List<Request> = requestsRepository.loadWeatherCards()
}