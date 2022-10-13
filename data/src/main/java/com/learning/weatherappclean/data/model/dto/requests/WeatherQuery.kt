package com.learning.weatherappclean.data.model.dto.requests


import com.squareup.moshi.Json

data class WeatherQuery(
    @field:Json(name = "content")
    val content: List<CardQuery>
)