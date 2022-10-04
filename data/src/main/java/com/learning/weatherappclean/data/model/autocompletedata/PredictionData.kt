package com.learning.weatherappclean.data.model.autocompletedata
import com.squareup.moshi.Json


data class PredictionData(
    @field:Json( name ="country")
    val country: String,
    @field:Json( name = "lat")
    val lat: String,
    @field:Json( name = "lon")
    val lon: String,
    @field:Json( name ="name")
    val name: String,
    @field:Json( name = "region")
    val region: String,
    @field:Json( name = "timezone_id")
    val timezoneId: String,
    @field:Json( name ="utc_offset")
    val utcOffset: String
)