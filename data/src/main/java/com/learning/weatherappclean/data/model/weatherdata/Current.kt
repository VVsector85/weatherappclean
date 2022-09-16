package com.learning.weatherappclean.data.model.weatherdata


import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("cloudcover")
    val cloudcover: String,
    @SerializedName("feelslike")
    val feelslike: String,
    @SerializedName("humidity")
    val humidity: String,
    @SerializedName("observation_time")
    val observationTime: String,
    @SerializedName("precip")
    val precip: String,
    @SerializedName("pressure")
    val pressure: String,
    @SerializedName("temperature")
    val temperature: String,
    @SerializedName("uv_index")
    val uvIndex: String,
    @SerializedName("visibility")
    val visibility: String,
    @SerializedName("weather_code")
    val weatherCode: String,
    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String>,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>,
    @SerializedName("wind_degree")
    val windDegree: String,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: String
)