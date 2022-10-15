package com.learning.weatherappclean.data.model.dto.weatherdata



import com.squareup.moshi.Json

data class Current(
    @field:Json( name ="cloudcover")
    val cloudCover: String,
    @field:Json( name ="feelslike")
    val feelsLike: String,
    val humidity: String,
    @field:Json( name ="observation_time")
    val observationTime: String,
    val precip: String,
    val pressure: String,
    val temperature: String,
    @field:Json( name ="uv_index")
    val uvIndex: String,
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