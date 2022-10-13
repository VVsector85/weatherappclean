package com.learning.weatherappclean.data.model.dto.weatherdata



import com.squareup.moshi.Json

data class Location(
    @field:Json( name ="country")
    val country: String,
    @field:Json( name ="lat")
    val lat: String,
    @field:Json( name ="localtime")
    val localtime: String,
    @field:Json( name ="localtime_epoch")
    val localtimeEpoch: Int,
    @field:Json( name ="lon")
    val lon: String,
    @field:Json( name ="name")
    val name: String,
    @field:Json( name ="region")
    val region: String,
    @field:Json( name ="timezone_id")
    val timezoneId: String,
    @field:Json( name ="utc_offset")
    val utcOffset: String
)