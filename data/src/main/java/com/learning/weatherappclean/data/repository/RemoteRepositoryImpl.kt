package com.learning.weatherappclean.data.repository


import android.gesture.Prediction
import com.learning.weatherappclean.data.model.autocompletedata.PredictionData
import com.learning.weatherappclean.data.util.Constants.API_KEY
import com.learning.weatherappclean.data.util.Constants.WEATHER_UNITS
import com.learning.weatherappclean.data.model.weatherdata.Weather
import com.learning.weatherappclean.data.remote_source.WeatherApi
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository


class RemoteRepositoryImpl (private val weatherApi: WeatherApi) :RemoteRepository {
    override suspend fun getWeatherData(location:WeatherCard): WeatherCard {


            return mapWeatherDataToDomain(weatherApi.getWeather(accessKey = API_KEY,city = location.location,units = WEATHER_UNITS))




    }
    override suspend fun getAutocompletePredictions(searchString:AutocompletePrediction):AutocompletePrediction{
        return mapAutocompletePredictionDataToDomain(weatherApi.getAutocomplete(accessKey = API_KEY,searchString = searchString.searchString))
    }

    private fun mapWeatherDataToDomain(response: Weather):WeatherCard{
         return WeatherCard(location = response.location.name, temperature = response.current.temperature)
    }
    private fun mapAutocompletePredictionDataToDomain(response: PredictionData):AutocompletePrediction {
        val tempList :MutableList<String> = mutableListOf()
            response.results.forEach{
                tempList.add("${it.name}, ${it.country}")
            }
        return AutocompletePrediction(searchString = response.request.query,
            predictions = tempList)
    }





}