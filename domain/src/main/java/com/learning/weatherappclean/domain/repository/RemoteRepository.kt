package com.learning.weatherappclean.domain.repository

import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.AutocompleteRequest
import com.learning.weatherappclean.domain.model.ResourceDomain
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.model.WeatherRequest

interface RemoteRepository {
    suspend fun getWeatherData(weatherRequest: WeatherRequest): ResourceDomain<WeatherCard>
    suspend fun getAutocompletePredictions(autocompleteRequest: AutocompleteRequest): ResourceDomain<List<AutocompletePrediction>>
}
