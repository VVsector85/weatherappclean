package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.ResourceDomain
import com.learning.weatherappclean.domain.repository.RemoteRepository

class GetAutocompletePredictionsUseCase (private val remoteRepository: RemoteRepository) {

    suspend operator fun invoke(request:Request): ResourceDomain<List<AutocompletePrediction>> {
          return  remoteRepository.getAutocompletePredictions(request)
    }
}