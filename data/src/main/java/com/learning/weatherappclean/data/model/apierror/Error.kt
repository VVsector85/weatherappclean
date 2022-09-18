package com.learning.weatherappclean.data.model.apierror


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json


data class Error(
    @field:Json( name ="code")
    val code: Int,
    @field:Json( name = "info")
    val info: String,
    @field:Json( name = "type")
    val type: String
)