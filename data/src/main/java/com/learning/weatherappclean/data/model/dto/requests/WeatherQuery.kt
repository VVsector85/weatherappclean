package com.learning.weatherappclean.data.model.dto.requests

import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.model.WeatherRequest

data class WeatherQuery(
    val content: List<CardQuery>
)

internal fun WeatherQuery.mapToDomain(): List<WeatherRequest> {
    val list: MutableList<WeatherRequest> = mutableListOf()
    this.content.forEach {
        list.add(
            WeatherRequest(
                query = "${it.location}, ${it.country}, ${it.region}",
                location = it.location,
                country = it.country,
                region = it.region,
                lat = it.lat,
                lon = it.lon,
                showDetails = it.isDetailed,
                units = null
            )
        )
    }
    return list
}

internal fun List<WeatherCard>.mapToStorage(): WeatherQuery {
    val list: MutableList<CardQuery> = mutableListOf()
    this.forEach {
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
