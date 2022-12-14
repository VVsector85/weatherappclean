package com.learning.weatherappclean.data.source.local.sharedprefs

import android.content.Context
import com.learning.weatherappclean.data.model.dto.requests.WeatherQuery
import com.learning.weatherappclean.data.source.local.RequestsStorage
import com.learning.weatherappclean.data.util.JsonConverter

class SharedPrefsRequestsStorage(context: Context) : RequestsStorage {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_WEATHER_CARDS, Context.MODE_PRIVATE)

    override fun save(weatherQuery: WeatherQuery): Boolean {
        sharedPreferences.edit().putString(KEY_REQUESTS, JsonConverter().convertToJson(weatherQuery)).apply()
        return true
    }

    override fun load(): WeatherQuery =
        JsonConverter().convertFromJson<WeatherQuery>(jsonString = sharedPreferences.getString(KEY_REQUESTS, DEFAULT_REQUEST_LIST) ?: DEFAULT_REQUEST_LIST)!!

    companion object {
        private const val SHARED_PREFS_WEATHER_CARDS = "shared_prefs_weather_cards"
        private const val KEY_REQUESTS = "requests"
        private const val DEFAULT_REQUEST_LIST = """{"content":[{"location":"Stockholm","country":"Sweden","region":"Stockholms Lan","lat":"59.333","lon":"18.050","isDetailed":false}]}"""
    }
}
