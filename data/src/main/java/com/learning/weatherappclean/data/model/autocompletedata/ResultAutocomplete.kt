package com.learning.weatherappclean.data.model.autocompletedata


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json


data class ResultAutocomplete(
    @field:Json( name ="request")
    val request: Request,
    @field:Json( name ="results")
    val predictionData: List<PredictionData>
)