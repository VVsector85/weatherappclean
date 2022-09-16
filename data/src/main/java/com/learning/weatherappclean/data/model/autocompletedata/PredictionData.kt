package com.learning.weatherappclean.data.model.autocompletedata


import com.google.gson.annotations.SerializedName

data class PredictionData(
    @SerializedName("request")
    val request: Request,
    @SerializedName("results")
    val results: List<ResultAutocomplete>
)