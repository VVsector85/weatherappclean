package com.learning.weatherappclean.data.souce.local.sharedprefs

import android.content.Context
import android.util.Log
import com.learning.weatherappclean.data.model.requests.CardQuery
import com.learning.weatherappclean.data.souce.local.RequestsStorage
import com.learning.weatherappclean.data.model.requests.WeatherQuery
import com.learning.weatherappclean.data.util.JsonConverter
import com.learning.weatherappclean.data.util.Mapper

private const val SHARED_PREFS_WEATHER_CARDS = "shared_refs_weather_cards_22"
private const val KEY_REQUESTS = "requests"
private const val DEFAULT_REQUEST_LIST = """{"content":[{"location":"Stockholm","country":"Sweden","region":"Stockholms Lan","lat":"59.333","lon":"18.050","isDetailed":false}]}"""
    //JsonConverter().convertToJson(WeatherQuery(listOf(CardQuery("59.333","18.050","Stockholm","","",false))))//

class SharedPrefsRequestsStorage (context: Context): RequestsStorage {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_WEATHER_CARDS, Context.MODE_PRIVATE)

    override fun save(weatherQuery: WeatherQuery): Boolean {
        sharedPreferences.edit().putString(KEY_REQUESTS,JsonConverter().convertToJson(weatherQuery)).apply()
        return true
    }

    override fun load(): WeatherQuery =
         JsonConverter().convertFromJson<WeatherQuery>(jsonString =  sharedPreferences.getString(KEY_REQUESTS,DEFAULT_REQUEST_LIST)?:DEFAULT_REQUEST_LIST)!!


}