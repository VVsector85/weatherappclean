package com.learning.weatherappclean.data.storage

import com.learning.weatherappclean.data.storage.model.WeatherCardData

interface LocalStorage {
    fun save(weatherCardData: WeatherCardData): Boolean
    fun get(): WeatherCardData
}