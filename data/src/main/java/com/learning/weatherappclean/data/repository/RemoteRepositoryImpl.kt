package com.learning.weatherappclean.data.repository


import com.learning.weatherappclean.data.model.apierror.connection.ErrorType
import com.learning.weatherappclean.data.util.Mapper
import com.learning.weatherappclean.data.util.JsonConverter
import com.learning.weatherappclean.data.model.apierror.internal.ErrorResponse
import com.learning.weatherappclean.data.model.autocompletedata.AutocompleteResponse
import com.learning.weatherappclean.data.util.Constants.API_KEY
import com.learning.weatherappclean.data.model.weatherdata.WeatherResponse
import com.learning.weatherappclean.data.souce.remote.Resource
import com.learning.weatherappclean.data.souce.remote.WeatherApi
import com.learning.weatherappclean.domain.model.Autocomplete
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository
import javax.inject.Inject


class RemoteRepositoryImpl @Inject constructor(private val weatherApi: WeatherApi) :
    RemoteRepository, BaseRepository() {

    override suspend fun getWeatherData(request: Request): WeatherCard {
        val response =
            safeApiCall {
                weatherApi.getWeather(
                    accessKey = API_KEY,
                    city = request.request,
                    units = request.units
                )
            }
        if (response is Resource.Error) return WeatherCard(
            error = true,
            errorType = response.type,
            errorMsg = response.message ?: "error"
        )
        val weatherResponse = JsonConverter().convertFromJson<WeatherResponse>(response.data)
        return if (weatherResponse?.current != null) {
            Mapper().mapToDomain(weatherResponse)
        } else {
            val errorResponse = JsonConverter().convertFromJson<ErrorResponse>(response.data)
            if (errorResponse != null) {
                Mapper().mapToDomain<WeatherCard>(errorResponse) as WeatherCard
            } else WeatherCard(error = true, errorType = ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun getAutocompletePredictions(request: Request): Autocomplete {
        val response =
            safeApiCall {
                weatherApi.getAutocomplete(
                    accessKey = API_KEY,
                    searchString = request.request
                )
            }
        if (response is Resource.Error) return Autocomplete(
            error = true,
            errorType = response.type,
            errorMsg = response.message ?: "error"
        )
        val autocompleteResponse =
            JsonConverter().convertFromJson<AutocompleteResponse>(response.data)
        return if (autocompleteResponse?.predictionData != null) {
            Mapper().mapToDomain(autocompleteResponse)
        } else {
            val errorResponse = JsonConverter().convertFromJson<ErrorResponse>(response.data)
            if (errorResponse != null) {
                Mapper().mapToDomain<Autocomplete>(errorResponse) as Autocomplete
            } else Autocomplete(error = true, errorType = ErrorType.UNKNOWN_ERROR)
        }
    }
}



