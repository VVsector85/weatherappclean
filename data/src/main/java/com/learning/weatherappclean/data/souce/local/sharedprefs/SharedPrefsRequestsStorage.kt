package com.learning.weatherappclean.data.souce.local.sharedprefs

import android.content.Context
import com.learning.weatherappclean.data.souce.local.RequestsStorage
import com.learning.weatherappclean.data.model.requests.WeatherRequests
import com.learning.weatherappclean.data.util.JsonConverter

private const val SHARED_PREFS_WEATHER_CARDS = "shared_refs_weather_cards_11"
private const val KEY_REQUESTS = "requests"
private const val DEFAULT_REQUEST_LIST = """{"content": ["Athens","Kharkiv","Stockholm"]}"""
class SharedPrefsRequestsStorage (context: Context): RequestsStorage {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_WEATHER_CARDS, Context.MODE_PRIVATE)

    override fun save(weatherRequests: WeatherRequests): Boolean {
        sharedPreferences.edit().putString(KEY_REQUESTS,JsonConverter().convertToJson(weatherRequests)).apply()
        return true
    }

    override fun load(): WeatherRequests =
         JsonConverter().convertFromJson<WeatherRequests>(jsonString =  sharedPreferences.getString(KEY_REQUESTS,DEFAULT_REQUEST_LIST)?:DEFAULT_REQUEST_LIST)!!


}