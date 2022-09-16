package com.learning.weatherappclean.data.model.autocompletedata


import com.google.gson.annotations.SerializedName

data class ResultAutocomplete(
    @SerializedName("country")
    val country: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("timezone_id")
    val timezoneId: String,
    @SerializedName("utc_offset")
    val utcOffset: String
)