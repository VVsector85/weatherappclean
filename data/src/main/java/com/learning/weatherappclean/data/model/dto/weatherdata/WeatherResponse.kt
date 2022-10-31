package com.learning.weatherappclean.data.model.dto.weatherdata

import com.learning.weatherappclean.domain.model.WeatherCard

data class WeatherResponse(
    val current: Current,
    val location: Location,
    val request: Request,
)

internal fun WeatherResponse.mapToDomain(): WeatherCard {
    return WeatherCard(
        location = this.location.name,
        country = this.location.country,
        region = this.location.region,
        lat = this.location.lat,
        lon = this.location.lon,
        units = this.request.unit,
        temperature = this.current.temperature,
        cloudCover = this.current.cloudCover,
        feelsLike = this.current.feelsLike,
        humidity = this.current.humidity,
        pressure = this.current.pressure,
        uvIndex = this.current.uvIndex,
        windSpeed = this.current.windSpeed,
        weatherCode = this.current.weatherCode,
        isNightIcon = this.current.isDay == "no",
        weatherDescription = this.current.weatherDescriptions[0],
        cardColorOption = null,
        showDetails = false
    )
}
