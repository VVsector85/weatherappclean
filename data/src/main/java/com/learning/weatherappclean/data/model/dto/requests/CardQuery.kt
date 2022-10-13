package com.learning.weatherappclean.data.model.dto.requests

import com.squareup.moshi.Json

data class CardQuery(

    @field:Json( name ="lat")
    val lat:String,
    @field:Json( name ="lon")
    val lon:String,
    @field:Json( name ="location")
    val location:String,
    @field:Json( name ="country")
    val country:String,
    @field:Json( name ="region")
    val region:String,
    @field:Json( name ="isDetailed")
    val isDetailed:Boolean
)
