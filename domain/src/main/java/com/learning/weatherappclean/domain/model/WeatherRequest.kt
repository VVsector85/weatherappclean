package com.learning.weatherappclean.domain.model

data class WeatherRequest(
    val query: String,
    var units: String?,
    val location: String? = null,
    val country: String? = null,
    val region: String? = null,
    val lon: String? = null,
    val lat: String? = null,
    val showDetails: Boolean? = null

)
