package com.learning.weatherappclean.data.model.requests

import com.squareup.moshi.Json

data class CardQuery(
    @field:Json( name ="location")
    val location:String,
    @field:Json( name ="lat")
    val lat:String,
    @field:Json( name ="lon")
    val lon:String,
    @field:Json( name ="isDetailed")
    val isDetailed:Boolean
)
