package com.learning.weatherappclean.data.model.dto.autocompletedata
import com.squareup.moshi.Json

data class Request(
    val query: String,
    val results: Int
)