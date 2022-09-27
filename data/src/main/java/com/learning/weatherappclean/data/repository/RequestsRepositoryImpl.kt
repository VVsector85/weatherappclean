package com.learning.weatherappclean.data.repository

import com.learning.weatherappclean.data.souce.local.RequestsStorage
import com.learning.weatherappclean.data.util.Mapper
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RequestsRepository

class RequestsRepositoryImpl(private val requestsStorage: RequestsStorage) : RequestsRepository {

    override fun saveWeatherCards(saveWeatherCardsList: List<WeatherCard>): Boolean =
              requestsStorage.save(Mapper().mapToStorage(saveWeatherCardsList))


    override fun loadWeatherCards():List<Request> =
        Mapper().mapToDomain(requestsStorage.load())



}