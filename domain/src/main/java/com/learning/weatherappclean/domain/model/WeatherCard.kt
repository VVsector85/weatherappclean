package com.learning.weatherappclean.domain.model

data class WeatherCard(
    val location: String,
    val temperature: String="",
    val cloudCover: String= "",
    val feelsLike: String= "",
    val humidity: String= "",
    val observationTime: String= "",
    val pressure: String= "",
    val uvIndex: String= "",
    val visibility: String= "",
    val weatherCode: String= "",
    val windDegree: String= "",
    val windDir: String= "",
    val windSpeed: String = "",

    val country: String = "",
    val region: String= "",
    val lat: String= "",
    val lon: String= "",

    val localtime: String= "",
    val timezoneId: String= "",
    val utcOffset: String= "",

    val error: Boolean = false,
    val errorMsg: String=""




) {

}