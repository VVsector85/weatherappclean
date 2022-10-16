package com.learning.weatherappclean.domain.repository

import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard

interface RequestsRepository {
    fun saveWeatherCards(saveWeatherCardsList: List<WeatherCard>): Boolean
    fun loadWeatherCards(): List<Request>
}
