package com.learning.weatherappclean.data.model.dto.requests

data class CardQuery(
    val lat: String,
    val lon: String,
    val location: String,
    val country: String,
    val region: String,
    val isDetailed: Boolean
)
