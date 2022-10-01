package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.Autocomplete
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.repository.RemoteRepository

class GetAutocompletePredictionsUseCase (private val remoteRepository: RemoteRepository) {

    suspend fun execute(request:Request): Autocomplete {

          return  remoteRepository.getAutocompletePredictions(request)

    }
}