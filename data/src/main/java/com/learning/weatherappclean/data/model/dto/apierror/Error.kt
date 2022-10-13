package com.learning.weatherappclean.data.model.dto.apierror



import com.squareup.moshi.Json


data class Error(
    @field:Json( name ="code")
    val code: Int,
    @field:Json( name = "info")
    val info: String,
    @field:Json( name = "type")
    val type: String
)