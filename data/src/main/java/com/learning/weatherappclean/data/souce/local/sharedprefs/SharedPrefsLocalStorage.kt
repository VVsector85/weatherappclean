package com.learning.weatherappclean.data.souce.local.sharedprefs

import android.content.Context
import com.learning.weatherappclean.data.souce.local.LocalStorage
import com.learning.weatherappclean.data.model.WeatherRequests

private const val SHARED_PREFS_WEATHER_CARDS = "shared_refs_weather_cards_6"
private const val KEY_LOCATION_SET = "location_set"
private val DEFAULT_LOCATION_SET = setOf("2@Stockholm", "0@Kharkiv", "1@Athens")
class SharedPrefsLocalStorage (context: Context): LocalStorage {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_WEATHER_CARDS, Context.MODE_PRIVATE)

    override fun save(weatherRequests: WeatherRequests): Boolean {
        sharedPreferences.edit().putStringSet(KEY_LOCATION_SET,weatherRequests.content).apply()
        return true
    }

    override fun get(): WeatherRequests {
        return WeatherRequests(content=sharedPreferences
            .getStringSet(KEY_LOCATION_SET,DEFAULT_LOCATION_SET)?:DEFAULT_LOCATION_SET)
    }

}