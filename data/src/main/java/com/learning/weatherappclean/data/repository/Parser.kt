package com.learning.weatherappclean.data.repository

import android.util.Log
import com.learning.weatherappclean.data.souce.remote.Resource
import com.squareup.moshi.Moshi

internal class Parser {

   internal inline fun <reified T> convertBody(response: Resource<String>): T? {
        return try {
            Log.d("my_tag", "convert to error " + response.data)
            response.data.let {
                val moshiAdapter = Moshi.Builder().build().adapter(T::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}