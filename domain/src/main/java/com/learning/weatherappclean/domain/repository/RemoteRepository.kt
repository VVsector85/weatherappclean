package com.learning.weatherappclean.domain.repository

interface RemoteRepository {
    fun getWeatherData()
    fun getAutocompletePredictions()
}