package com.learning.weatherappclean.data.repository

import com.learning.weatherappclean.data.model.dto.requests.mapToDomain
import com.learning.weatherappclean.data.model.dto.requests.mapToStorage
import com.learning.weatherappclean.data.source.local.RequestsStorage
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.model.WeatherRequest
import com.learning.weatherappclean.domain.repository.RequestsRepository

class RequestsRepositoryImpl(private val requestsStorage: RequestsStorage) : RequestsRepository {

    override fun saveWeatherCards(saveWeatherCardsList: List<WeatherCard>): Boolean =
        requestsStorage.save(saveWeatherCardsList.mapToStorage())

    override fun loadWeatherCards(): List<WeatherRequest> =
        requestsStorage.load().mapToDomain()
}
