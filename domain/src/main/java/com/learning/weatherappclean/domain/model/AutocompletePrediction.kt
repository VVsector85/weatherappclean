package com.learning.weatherappclean.domain.model

data class AutocompletePrediction(
    val location: String,
    val country: String,
    val region: String,
    val lat: String,
    val lon: String
)