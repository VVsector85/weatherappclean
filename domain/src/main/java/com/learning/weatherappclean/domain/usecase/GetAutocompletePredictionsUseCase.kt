package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository

class GetAutocompletePredictionsUseCase (private val remoteRepository: RemoteRepository) {

    suspend fun execute(searchString: AutocompletePrediction): AutocompletePrediction {
        return if (searchString.searchString.length >= 3) {
            remoteRepository.getAutocompletePredictions(searchString)
        } else {

            AutocompletePrediction("")
        }
    }
}