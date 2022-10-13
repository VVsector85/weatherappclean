package com.learning.weatherappclean.data.util

import com.learning.weatherappclean.data.model.ErrorType
import com.learning.weatherappclean.data.model.dto.apierror.ErrorResponse
import com.learning.weatherappclean.data.model.dto.autocompletedata.AutocompleteResponse
import com.learning.weatherappclean.data.model.dto.requests.CardQuery
import com.learning.weatherappclean.data.model.dto.requests.WeatherQuery
import com.learning.weatherappclean.data.model.dto.settings.SettingsData
import com.learning.weatherappclean.data.model.dto.weatherdata.WeatherResponse
import com.learning.weatherappclean.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
            cardColorOption = CardColorOption.DEFAULT,
            showDetails = false
        )
    }


    internal fun mapToDomain(response: AutocompleteResponse): List<AutocompletePrediction> {
        val tempList: MutableList<AutocompletePrediction> = mutableListOf()
        response.predictionData.forEach {
            tempList.add(
                AutocompletePrediction(
                    location = it.name,
                    country = it.country,
                    region = it.region,
                    lat = it.lat,
                    lon = it.lon
                )
            )
        }

        return tempList
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