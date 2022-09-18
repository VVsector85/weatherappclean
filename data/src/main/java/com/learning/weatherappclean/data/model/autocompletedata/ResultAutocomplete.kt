package com.learning.weatherappclean.data.model.autocompletedata


import com.google.gson.annotations.SerializedName


data class ResultAutocomplete(
    @SerializedName( "request")
    val request: Request,
    @SerializedName("results")
    val predictionData: List<PredictionData>
)