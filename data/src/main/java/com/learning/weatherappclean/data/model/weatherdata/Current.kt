package com.learning.weatherappclean.data.model.weatherdata


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class Current(
    @field:Json( name ="cloudcover")
    val cloudcover: String,
    @field:Json( name ="feelslike")
    val feelslike: String,
    @field:Json( name ="humidity")
    val humidity: String,
    @field:Json( name ="observation_time")
    val observationTime: String,
    @field:Json( name ="precip")
    val precip: String,
    @field:Json( name ="pressure")
    val pressure: String,
    @field:Json( name ="temperature")
    val temperature: String,
    @field:Json( name ="uv_index")
    val uvIndex: String,
    @field:Json( name ="visibility")
    val visibility: String,
    @field:Json( name ="weather_code")
    val weatherCode: String,
    @field:Json( name ="weather_descriptions")
    val weatherDescriptions: List<String>,
    @field:Json( name ="weather_icons")
    val weatherIcons: List<String>,
    @field:Json( name ="wind_degree")
    val windDegree: String,
    @field:Json( name ="wind_dir")
    val windDir: String,
    @field:Json( name ="wind_speed")
    val windSpeed: String
)