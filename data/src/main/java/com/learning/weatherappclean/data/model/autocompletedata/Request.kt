package com.learning.weatherappclean.data.model.autocompletedata


import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("query")
    val query: String,
    @SerializedName("results")
    val results: Int
)