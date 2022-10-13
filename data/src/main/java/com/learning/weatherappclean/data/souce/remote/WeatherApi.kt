package com.learning.weatherappclean.data.souce.remote


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/current?")
    suspend fun getWeather(
        @Query("query") city: String,
        @Query("units") units: String
    ): Response<String>

    @GET("/autocomplete")
    suspend fun getAutocomplete(@Query("query") searchString: String): Response<String>


}