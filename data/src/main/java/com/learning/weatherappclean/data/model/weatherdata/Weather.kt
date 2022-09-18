package com.learning.weatherappclean.data.model.weatherdata


import com.google.gson.annotations.SerializedName
import com.learning.weatherappclean.data.model.apierror.Error
import com.squareup.moshi.Json

data class Weather(
    @field:Json( name ="current")
    val current: Current,
    @field:Json( name ="location")
    val location: Location,
    @field:Json( name ="request")
    val request: Request,


)
