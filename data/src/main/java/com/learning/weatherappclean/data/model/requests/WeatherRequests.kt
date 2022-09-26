package com.learning.weatherappclean.data.model.requests

import com.squareup.moshi.Json

 data class WeatherRequests(

    @field:Json( name ="content")
    val content : List<String>) {

}