package com.learning.weatherappclean.data.souce.local

import com.learning.weatherappclean.data.model.WeatherRequests

interface LocalStorage {
    fun save(weatherRequests: WeatherRequests): Boolean
    fun get(): WeatherRequests
}