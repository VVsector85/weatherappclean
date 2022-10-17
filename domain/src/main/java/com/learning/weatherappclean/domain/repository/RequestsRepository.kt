package com.learning.weatherappclean.domain.repository

import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.model.WeatherRequest

interface RequestsRepository {
    fun saveWeatherCards(saveWeatherCardsList: List<WeatherCard>): Boolean
    fun loadWeatherCards(): List<WeatherRequest>
}
