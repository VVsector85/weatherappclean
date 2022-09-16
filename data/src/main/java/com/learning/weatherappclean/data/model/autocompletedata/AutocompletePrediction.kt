package com.learning.weatherappclean.data.model.autocompletedata


import com.google.gson.annotations.SerializedName

data class AutocompletePrediction(
    @SerializedName("request")
    val request: Request,
    @SerializedName("results")
    val results: List<ResultWs>
)