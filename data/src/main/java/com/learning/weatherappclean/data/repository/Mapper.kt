package com.learning.weatherappclean.data.repository

import android.util.Log
import com.learning.weatherappclean.data.model.apierror.internal.ErrorResponse
import com.learning.weatherappclean.data.model.autocompletedata.AutocompleteResponse
import com.learning.weatherappclean.data.model.weatherdata.WeatherResponse
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.WeatherCard

internal class Mapper {

    internal fun mapToDomain(response: WeatherResponse): WeatherCard {
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
            timezoneId = response.location.timezoneId,
            utcOffset = response.location.utcOffset,


            )
    }


    internal inline fun <reified T> mapToDomain(response: ErrorResponse): Any? {
        return when (T::class) {
            WeatherCard::class -> WeatherCard(
                location = "none",
                error = true,
                errorMsg = "Error code: ${response.error.code}, ${response.error.type}, ${response.error.info}"
            )
            AutocompletePrediction::class -> AutocompletePrediction(

                error = true,
                errorMsg = "Error code: ${response.error.code}, ${response.error.type}, ${response.error.info}"
            )
            else -> null
        }
    }

    internal fun mapToDomain(response: AutocompleteResponse): AutocompletePrediction {
        val tempList: MutableList<AutocompletePrediction.Predictions> = mutableListOf()
        response.predictionData.forEach {
            Log.d("my_tag", "prediction item ${it.name}, ${it.country}")
            tempList.add(AutocompletePrediction.Predictions(it.name,it.country,it.region))
        }

        return AutocompletePrediction(
            searchString = response.request.query,
            predictions = tempList
        )
    }


}