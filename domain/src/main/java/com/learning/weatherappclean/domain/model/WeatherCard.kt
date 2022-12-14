package com.learning.weatherappclean.domain.model

data class WeatherCard(
    val location: String,
    val country: String,
    val region: String,
    val temperature: String,
    val units: String,
    val cloudCover: String,
    val feelsLike: String,
    val humidity: String,
    val pressure: String,
    val uvIndex: String,
    val windSpeed: String,
    val weatherCode: String,
    val lat: String,
    val lon: String,
    val weatherDescription: String,
    val isNightIcon: Boolean,
    var showDetails: Boolean,
    var cardColorOption: CardColorOption?
)
