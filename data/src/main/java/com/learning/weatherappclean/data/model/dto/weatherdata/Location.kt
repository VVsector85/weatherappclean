package com.learning.weatherappclean.data.model.dto.weatherdata



import com.squareup.moshi.Json

data class Location(
    val country: String,
    val lat: String,
    val localtime: String,
    @field:Json( name ="localtime_epoch")
    val localtimeEpoch: Int,
    val lon: String,
    val name: String,
    val region: String,
    @field:Json( name ="timezone_id")
    val timezoneId: String,
    @field:Json( name ="utc_offset")
    val utcOffset: String
)