package com.learning.weatherappclean.data.model.requests


import com.squareup.moshi.Json

data class WeatherQuery(

    @field:Json(name = "content")
    val content: List<CardQuery>
)