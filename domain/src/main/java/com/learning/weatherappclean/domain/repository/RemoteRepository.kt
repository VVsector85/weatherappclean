package com.learning.weatherappclean.domain.repository

import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.WeatherCard

interface RemoteRepository {
   suspend fun getWeatherData(location:WeatherCard) : WeatherCard
   suspend fun getAutocompletePredictions():AutocompletePrediction
}