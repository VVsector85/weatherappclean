package com.learning.weatherappclean.domain.repository

import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard

interface RemoteRepository {
   suspend fun getWeatherData(request: Request) : WeatherCard
   suspend fun getAutocompletePredictions(request: Request):AutocompletePrediction


}