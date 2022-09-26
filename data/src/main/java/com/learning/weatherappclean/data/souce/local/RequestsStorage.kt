package com.learning.weatherappclean.data.souce.local

import com.learning.weatherappclean.data.model.requests.WeatherRequests

interface RequestsStorage {
    fun save(weatherRequests: WeatherRequests): Boolean
    fun get(): WeatherRequests
}