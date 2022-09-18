package com.learning.weatherappclean.data.repository


import android.gesture.Prediction
import android.util.Log
import com.learning.weatherappclean.data.model.autocompletedata.PredictionData
import com.learning.weatherappclean.data.model.autocompletedata.ResultAutocomplete
import com.learning.weatherappclean.data.util.Constants.API_KEY
import com.learning.weatherappclean.data.util.Constants.WEATHER_UNITS
import com.learning.weatherappclean.data.model.weatherdata.Weather
import com.learning.weatherappclean.data.remote_source.WeatherApi
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository


class RemoteRepositoryImpl(private val weatherApi: WeatherApi) : RemoteRepository {

    override suspend fun getWeatherData(location: WeatherCard): WeatherCard {
        Log.d("my_tag", "weather query")
        val result = weatherApi.getWeather(
            accessKey = API_KEY,
            city = location.location,
            units = WEATHER_UNITS
        )
        Log.d("my_tag", "resultIsSuccesful"+result.isSuccessful.toString()+result.message())
        if (result.isSuccessful) {
            return mapWeatherDataToDomain(result.body()!!)
        }
        else{
            Log.d("my_tag",result.message()+"UUUUU")
            throw Exception(result.message())
        }

    }

    override suspend fun getAutocompletePredictions(searchString: AutocompletePrediction): AutocompletePrediction {
        val result =weatherApi.getAutocomplete(
            accessKey = API_KEY,
            searchString = searchString.searchString
        )
        if (result.isSuccessful) {
            return mapAutocompletePredictionDataToDomain(result.body()!!)
        }else{
            throw Exception(result.message())
        }
    }

    private fun mapWeatherDataToDomain(response: Weather): WeatherCard {
        return WeatherCard(
            location = response.location.name,
            temperature = response.current.temperature,
            cloudCover = response.current.cloudcover,
            feelsLike = response.current.feelslike,
            humidity = response.current.humidity,
            observationTime = response.current.observationTime,
            pressure = response.current.pressure,
            uvIndex = response.current.uvIndex,
            visibility = response.current.visibility,
            weatherCode = response.current.weatherCode,
            windDegree = response.current.windDegree,
            windDir = response.current.windDir,
            windSpeed = response.current.windSpeed,

            country = response.location.country,
            region = response.location.region,
            lat = response.location.lat,
            lon = response.location.lon,

            localtime = response.location.localtime,
            timezoneId= response.location.timezoneId,
            utcOffset= response.location.utcOffset

        )
    }

    private fun mapAutocompletePredictionDataToDomain(response: ResultAutocomplete): AutocompletePrediction {
        val tempList: MutableList<String> = mutableListOf()
        response.predictionData.forEach {
            tempList.add("${it.name}, ${it.country}")
        }
        return AutocompletePrediction(
            searchString = response.request.query,
            predictions = tempList
        )
    }


}