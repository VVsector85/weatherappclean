package com.learning.weatherappclean.data.local_source.sharedprefs

import android.content.Context
import com.learning.weatherappclean.data.local_source.LocalStorage
import com.learning.weatherappclean.data.model.WeatherCardData

private const val SHARED_PREFS_WEATHER_CARDS = "shared_refs_weather_cards_4"
private const val KEY_LOCATION_SET = "location_set"
private val DEFAULT_LOCATION_SET = setOf("2@Stockholm", "0@Kharkiv", "1@Athens", "3@Berlin")
class SharedPrefsLocalStorage (context: Context): LocalStorage {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_WEATHER_CARDS, Context.MODE_PRIVATE)

    override fun save(weatherCardData: WeatherCardData): Boolean {

        sharedPreferences.edit().putStringSet(KEY_LOCATION_SET,weatherCardData.content).apply()
        return true
    }

    override fun get(): WeatherCardData {
        return WeatherCardData(content=sharedPreferences
            .getStringSet(KEY_LOCATION_SET,DEFAULT_LOCATION_SET)?:DEFAULT_LOCATION_SET)
    }

}