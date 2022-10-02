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
            weatherDescription = response.current.weatherDescriptions[0],




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
            Autocomplete::class -> Autocomplete(

                error = true,
                errorType = ErrorType.API_ERROR,
                errorMsg = "Error code: ${response.error.code}, ${response.error.type}, ${response.error.info}"
            )
            else -> null
        }
    }

    internal fun mapToDomain(response: AutocompleteResponse): Autocomplete {
        val tempList: MutableList<Autocomplete.Predictions> = mutableListOf()
        response.predictionData.forEach {
            tempList.add(Autocomplete.Predictions(it.name, it.country, it.region,it.lat,it.lon))
        }

        return Autocomplete(
            searchString = response.request.query,
            predictions = tempList,
        )
    }

    internal fun mapToDomain(response: SettingsData): Settings =
        Settings(
            fahrenheit = response.fahrenheit,
            newCardFirst = response.newCardFirst,
            detailsOnDoubleTap = response.detailsOnDoubleTap,
            dragAndDropCards = response.detailsOnDoubleTap

        )
    internal fun mapToDomain(weatherQuery: WeatherQuery):List<Request>{
        val list: MutableList<Request> = mutableListOf()
        weatherQuery.content.forEach{list.add(Request(query = it.location, lat = it.lat, lon = it.lon, showDetails = it.isDetailed))}
        return list
    }
    internal fun mapToStorage(saveWeatherCardsList: List<WeatherCard>): WeatherQuery {
        val list: MutableList<CardQuery> = mutableListOf()
        //saveWeatherCardsList.forEach{list.add(/*"${ it.lat }, ${ it.lon }"*/"${it.location }, ${it.country}, ${it.region}")}
        saveWeatherCardsList.forEach{list.add(
            CardQuery(location = "${it.location }, ${it.country}, ${it.region}",lat = it.lat,lon = it.lon, isDetailed =  it.showDetails)
        )}
        return WeatherQuery(content = list)
    }

}