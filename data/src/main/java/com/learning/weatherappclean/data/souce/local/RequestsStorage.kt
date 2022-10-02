package com.learning.weatherappclean.data.souce.local

import com.learning.weatherappclean.data.model.requests.WeatherQuery

interface RequestsStorage {
    fun save(weatherQuery: WeatherQuery): Boolean
    fun load(): WeatherQuery
}