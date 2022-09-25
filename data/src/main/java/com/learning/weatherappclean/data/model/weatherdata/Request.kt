package com.learning.weatherappclean.data.model.weatherdata



import com.squareup.moshi.Json

data class Request(
    @field:Json( name ="language")
    val language: String,
    @field:Json( name ="query")
    val query: String,
    @field:Json( name ="type")
    val type: String,
    @field:Json( name ="unit")
    val unit: String
)