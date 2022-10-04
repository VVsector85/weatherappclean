package com.learning.weatherappclean.data.model.autocompletedata
import com.squareup.moshi.Json

data class Request(
    @field:Json( name = "query")
    val query: String,
    @field:Json( name ="results")
    val results: Int
)