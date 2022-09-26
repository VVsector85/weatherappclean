package com.learning.weatherappclean.data.util

import android.util.Log
import com.learning.weatherappclean.data.model.apierror.connection.ErrorType
import com.learning.weatherappclean.data.model.apierror.internal.ErrorResponse
import com.learning.weatherappclean.data.model.autocompletedata.AutocompleteResponse
import com.learning.weatherappclean.data.model.requests.WeatherRequests
import com.learning.weatherappclean.data.model.settings.SettingsData
import com.learning.weatherappclean.data.model.weatherdata.WeatherResponse
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard

internal class Mapper {

    internal fun mapToDomain(response: WeatherResponse): WeatherCard {
        return WeatherCard(
            location = response.location.name,
            temperature = response.current.temperature,
            units = response.request.unit,
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
            isNightIcon = response.current.weatherIcons[0].contains("night"),

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
                errorType = ErrorType.API_ERROR,
                errorMsg = "Error code: ${response.error.code}, ${response.error.type}, ${response.error.info}",

                )
            AutocompletePrediction::class -> AutocompletePrediction(

                error = true,
                errorType = ErrorType.API_ERROR,
                errorMsg = "Error code: ${response.error.code}, ${response.error.type}, ${response.error.info}"
            )
            else -> null
        }
    }

    internal fun mapToDomain(response: AutocompleteResponse): AutocompletePrediction {
        val tempList: MutableList<AutocompletePrediction.Predictions> = mutableListOf()
        response.predictionData.forEach {
            tempList.add(AutocompletePrediction.Predictions(it.name, it.country, it.region))
        }

        return AutocompletePrediction(
            searchString = response.request.query,
            predictions = tempList,
        )
    }

    internal fun mapToDomain(response: SettingsData): Settings =
        Settings(
            fahrenheit = response.fahrenheit,
            newCardFirst = response.newCardFirst,
            showFeelsLike = response.showFeelsLike,
            showCountry = response.showCountry

        )
    internal fun mapToDomain(weatherRequests: WeatherRequests):List<Request>{
        val list: MutableList<Request> = mutableListOf()
        weatherRequests.content.forEach{ it ->list.add(Request(request =it))}
        return list
    }
    internal fun mapToStorage(saveWeatherCardsList: List<WeatherCard>): WeatherRequests {
        val list: MutableList<String> = mutableListOf()
        saveWeatherCardsList.forEach{list.add("${it.location }, ${it.country}"   )}
        return WeatherRequests(content = list)
    }

}