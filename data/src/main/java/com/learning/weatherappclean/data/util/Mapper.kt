package com.learning.weatherappclean.data.util

import com.learning.weatherappclean.data.model.apierror.connection.ErrorType
import com.learning.weatherappclean.data.model.apierror.internal.ErrorResponse
import com.learning.weatherappclean.data.model.autocompletedata.AutocompleteResponse
import com.learning.weatherappclean.data.model.requests.CardQuery
import com.learning.weatherappclean.data.model.requests.WeatherQuery
import com.learning.weatherappclean.data.model.settings.SettingsData
import com.learning.weatherappclean.data.model.weatherdata.WeatherResponse
import com.learning.weatherappclean.domain.model.Autocomplete
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.model.WeatherCard

internal class Mapper {

    internal fun mapToDomain(response: WeatherResponse): WeatherCard {
        return WeatherCard(
            location = response.location.name,
            country = response.location.country,
            region = response.location.region,
            lat = response.location.lat,
            lon = response.location.lon,
            units = response.request.unit,

            temperature = response.current.temperature,
            cloudCover = response.current.cloudcover,
            feelsLike = response.current.feelslike,
            humidity = response.current.humidity,
            pressure = response.current.pressure,
            uvIndex = response.current.uvIndex,
            windSpeed = response.current.windSpeed,
            weatherCode = response.current.weatherCode,

            isNightIcon = response.current.weatherIcons[0].contains("night"),
            weatherDescription = response.current.weatherDescriptions[0],

            /*   observationTime = response.current.observationTime,
               visibility = response.current.visibility,
               windDegree = response.current.windDegree,
               windDir = response.current.windDir,
               localtime = response.location.localtime,
               timezoneId = response.location.timezoneId,
               utcOffset = response.location.utcOffset,*/


        )
    }


    internal inline fun <reified T> mapToDomain(response: ErrorResponse): Any? {
        return when (T::class) {
            WeatherCard::class -> WeatherCard(

                error = true,
                errorType = ErrorType.API_ERROR,
                errorCode = response.error.code,
                errorMsg = "${response.error.type}, ${response.error.info}",

                )
            Autocomplete::class -> Autocomplete(

                error = true,
                errorType = ErrorType.API_ERROR,
                errorCode = response.error.code,
                errorMsg = "${response.error.type}, ${response.error.info}"
            )
            else -> null
        }
    }

    internal fun mapToDomain(response: AutocompleteResponse): Autocomplete {
        val tempList: MutableList<Autocomplete.Prediction> = mutableListOf()
        response.predictionData.forEach {
            tempList.add(
                Autocomplete.Prediction(
                    location = it.name,
                    country = it.country,
                    region = it.region,
                    lat = it.lat,
                    lon = it.lon
                )
            )
        }

        return Autocomplete(
            searchString = response.request.query,
            predictions = tempList
        )
    }

    internal fun mapToDomain(response: SettingsData): Settings =
        Settings(
            imperialUnits = response.imperialUnits,
            newCardFirst = response.newCardFirst,
            detailsOnDoubleTap = response.detailsOnDoubleTap,
            dragAndDropCards = response.dragAndDropCards

        )

    internal fun mapToDomain(weatherQuery: WeatherQuery): List<Request> {
        val list: MutableList<Request> = mutableListOf()
        weatherQuery.content.forEach {
            list.add(
                Request(
                    query = "${it.location}, ${it.country}, ${it.region}",
                    location = it.location,
                    country = it.country,
                    region = it.region,
                    lat = it.lat,
                    lon = it.lon,
                    showDetails = it.isDetailed
                )
            )
        }
        return list
    }

    internal fun mapToStorage(saveWeatherCardsList: List<WeatherCard>): WeatherQuery {
        val list: MutableList<CardQuery> = mutableListOf()
        saveWeatherCardsList.forEach {
            list.add(
                CardQuery(
                    location = it.location,
                    country = it.country,
                    region = it.region,
                    lat = it.lat,
                    lon = it.lon,
                    isDetailed = it.showDetails
                )
            )
        }
        return WeatherQuery(content = list)
    }
}