package com.learning.weatherappclean.data.model.dto.weatherdata

import com.learning.weatherappclean.domain.model.WeatherCard

data class WeatherResponse(
    val current: Current,
    val location: Location,
    val request: Request,
)

internal fun WeatherResponse.mapToDomain(): WeatherCard {
    return WeatherCard(
        location = location.name,
        country = location.country,
        region = location.region,
        lat = location.lat,
        lon = location.lon,
        units = request.unit,
        temperature = current.temperature,
        cloudCover = current.cloudCover,
        feelsLike = current.feelsLike,
        humidity = current.humidity,
        pressure = current.pressure,
        uvIndex = current.uvIndex,
        windSpeed = current.windSpeed,
        weatherCode = current.weatherCode,
        isNightIcon = current.isDay == "no",
        weatherDescription = current.weatherDescriptions[0],
        cardColorOption = null,
        showDetails = false
    )
}
