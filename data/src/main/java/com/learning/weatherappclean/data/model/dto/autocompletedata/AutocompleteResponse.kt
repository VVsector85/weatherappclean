package com.learning.weatherappclean.data.model.dto.autocompletedata

import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.squareup.moshi.Json

data class AutocompleteResponse(
    val request: Request,
    @field:Json(name = "results")
    val predictionData: List<PredictionData>
)

internal fun AutocompleteResponse.mapToDomain(): List<AutocompletePrediction> {
    val tempList: MutableList<AutocompletePrediction> = mutableListOf()
    this.predictionData.forEach {
        tempList.add(
            AutocompletePrediction(
                location = it.name,
                country = it.country,
                region = it.region,
                lat = it.lat,
                lon = it.lon
            )
        )
    }
    return tempList
}
