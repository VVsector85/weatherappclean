package com.learning.weatherappclean.data.remote_source


import com.learning.weatherappclean.data.model.autocompletedata.PredictionData
import com.learning.weatherappclean.data.model.autocompletedata.ResultAutocomplete
import com.learning.weatherappclean.data.model.weatherdata.Weather
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/current?")
    suspend fun getWeather(@Query("access_key") accessKey: String,
                           @Query("query") city: String,
                           @Query("units") units:String) : Response<Weather>
    @GET("/autocomplete")
    suspend fun getAutocomplete(@Query("access_key") accessKey: String,
                           @Query("query") searchString: String) : Response<ResultAutocomplete>



}