package com.learning.weatherappclean.data.repository


import com.learning.weatherappclean.data.util.Mapper
import com.learning.weatherappclean.data.util.Parser
import com.learning.weatherappclean.data.model.apierror.internal.ErrorResponse
import com.learning.weatherappclean.data.model.autocompletedata.AutocompleteResponse
import com.learning.weatherappclean.data.util.Constants.API_KEY
import com.learning.weatherappclean.data.model.weatherdata.WeatherResponse
import com.learning.weatherappclean.data.souce.remote.Resource
import com.learning.weatherappclean.data.souce.remote.WeatherApi
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository
import javax.inject.Inject


class RemoteRepositoryImpl @Inject constructor (private val weatherApi: WeatherApi) : RemoteRepository, BaseRepository() {

    override suspend fun getWeatherData(request: Request): WeatherCard {
        val response =
        safeApiCall {
            weatherApi.getWeather(
                accessKey = API_KEY,
                city = request.request,
                units = request.units
            ) }
        if (response is Resource.Error) return WeatherCard(location = "",error = true, errorType = response.type, errorMsg = response.message?:"error")
        val weatherResponse = Parser().convertBody<WeatherResponse>(response)
        return if (weatherResponse?.current != null) {
            Mapper().mapToDomain(weatherResponse)
        } else {
            val resultError = Parser().convertBody<ErrorResponse>(response)
            Mapper().mapToDomain<WeatherCard>(resultError!!) as WeatherCard
        }
    }

    override suspend fun getAutocompletePredictions(request:Request): AutocompletePrediction {
        val response =
            safeApiCall{
                weatherApi.getAutocomplete(
                    accessKey = API_KEY,
                    searchString = request.request)
            }
        if (response is Resource.Error) return AutocompletePrediction(error = true, errorType = response.type, errorMsg = response.message?:"error")
        val autocompleteResponse = Parser().convertBody<AutocompleteResponse>(response)
        return if (autocompleteResponse?.predictionData != null) {
            Mapper().mapToDomain(autocompleteResponse)
        } else {
            val errorResponse = Parser().convertBody<ErrorResponse>(response)
            Mapper().mapToDomain<AutocompletePrediction>(errorResponse!!) as AutocompletePrediction

        }

    }

}



