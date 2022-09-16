package com.learning.weatherappclean.data.model.weatherdata


import com.google.gson.annotations.SerializedName
import com.learning.weatherappclean.data.model.weatherdata.Current
import com.learning.weatherappclean.data.model.weatherdata.Location
import com.learning.weatherappclean.data.model.weatherdata.Request

data class Weather(
    @SerializedName("current")
    val current: Current,
    @SerializedName("location")
    val location: Location,
    @SerializedName("request")
    val request: Request
)