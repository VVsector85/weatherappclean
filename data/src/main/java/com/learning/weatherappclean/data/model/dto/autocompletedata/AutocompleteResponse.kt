package com.learning.weatherappclean.data.model.dto.autocompletedata

import com.squareup.moshi.Json


data class AutocompleteResponse(
    @field:Json( name ="request")
    val request: Request,
    @field:Json( name ="results")
    val predictionData: List<PredictionData>
)