package com.learning.weatherappclean.data.storage.sharedprefs

import android.content.Context
import com.learning.weatherappclean.data.storage.LocalStorage
import com.learning.weatherappclean.data.storage.model.WeatherCardData

private const val SHARED_PREFS_WEATHER_CARD = "shared_refs_weather_card"
private const val KEY_LOCATION_SET = "location_set"
private const val KEY_LOCATION = "location"
private const val KEY_TEMPERATURE = "temperature"
private const val DEFAULT_LOCATION = "default location"
private const val DEFAULT_TEMPERATURE = "default temperature"
private val DEFAULT_LOCATION_SET = mutableSetOf("Stockholm", "Kharkiv", "Athens")
class SharedPrefsLocalStorage (context: Context): LocalStorage {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_WEATHER_CARD, Context.MODE_PRIVATE)

    override fun save(weatherCardData: WeatherCardData): Boolean {
        //sharedPreferences.edit().putString(KEY_LOCATION, weatherCardData.location).apply()
        //sharedPreferences.edit().putString(KEY_TEMPERATURE, weatherCardData.temperature).apply()
        sharedPreferences.edit().putStringSet(KEY_LOCATION_SET,weatherCardData.content).apply()
        return true
    }

    override fun get(): WeatherCardData {
        //val location = sharedPreferences.getString(KEY_LOCATION, DEFAULT_LOCATION)?: DEFAULT_LOCATION
        //val temperature = sharedPreferences.getString(KEY_TEMPERATURE, DEFAULT_TEMPERATURE)?: DEFAULT_TEMPERATURE
        return WeatherCardData(content=sharedPreferences
            .getStringSet(KEY_LOCATION_SET,DEFAULT_LOCATION_SET)?:DEFAULT_LOCATION_SET)
    }

}