package com.learning.weatherappclean.data.model.weatherdata


import com.squareup.moshi.Json

data class WeatherResponse(
    @field:Json( name ="current")
    val current: Current,
    @field:Json( name ="location")
    val location: Location,
    @field:Json( name ="request")
    val request: Request,


)
