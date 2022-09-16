package com.learning.weatherappclean.data.local_source

import com.learning.weatherappclean.data.model.WeatherCardData

interface LocalStorage {
    fun save(weatherCardData: WeatherCardData): Boolean
    fun get(): WeatherCardData
}