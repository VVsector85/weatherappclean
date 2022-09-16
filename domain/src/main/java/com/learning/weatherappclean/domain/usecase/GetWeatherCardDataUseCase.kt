package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository

class GetWeatherCardDataUseCase(private val remoteRepository: RemoteRepository) {

    suspend fun execute (location:WeatherCard):WeatherCard{

        return remoteRepository.getWeatherData(location)
    }
}