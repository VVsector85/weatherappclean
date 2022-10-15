package com.learning.weatherappclean.data.model.dto.autocompletedata
import com.squareup.moshi.Json

data class PredictionData(
    val country: String,
    val lat: String,
    val lon: String,
    val name: String,
    val region: String,
    @field:Json( name = "timezone_id")
    val timezoneId: String,
    @field:Json( name ="utc_offset")
    val utcOffset: String
)