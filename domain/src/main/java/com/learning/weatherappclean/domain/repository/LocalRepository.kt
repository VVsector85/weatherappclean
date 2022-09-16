package com.learning.weatherappclean.domain.repository

import com.learning.weatherappclean.domain.model.WeatherCard

interface LocalRepository {

    fun saveWeatherCards(saveWeatherCardsList: List<WeatherCard>):Boolean
    fun loadWeatherCards():List<WeatherCard>

}