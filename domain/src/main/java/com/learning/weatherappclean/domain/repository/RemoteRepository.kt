package com.learning.weatherappclean.domain.repository

import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.WeatherCard

interface RemoteRepository {
   suspend fun getWeatherData(location:WeatherCard) : WeatherCard
   suspend fun getAutocompletePredictions(searchString: AutocompletePrediction):AutocompletePrediction

/*I'm not sure if this is the right approach, but I wanted to minimize the number of entities,
so these functions receive and return the same type of data

Also I'd like to get rid of "suspend" in the interface, but have not found a way.
*/


}