package com.learning.weatherappclean.data.souce.local

import com.learning.weatherappclean.data.model.WeatherLocalData

interface LocalStorage {
    fun save(weatherLocalData: WeatherLocalData): Boolean
    fun get(): WeatherLocalData
}