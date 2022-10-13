package com.learning.weatherappclean.data.repository


import com.learning.weatherappclean.data.model.ErrorType
import com.learning.weatherappclean.data.util.Mapper
import com.learning.weatherappclean.data.util.JsonConverter
import com.learning.weatherappclean.data.model.dto.apierror.ErrorResponse
import com.learning.weatherappclean.data.model.dto.autocompletedata.AutocompleteResponse
import com.learning.weatherappclean.data.util.Constants.API_KEY
import com.learning.weatherappclean.data.model.dto.weatherdata.WeatherResponse
import com.learning.weatherappclean.data.souce.remote.Resource
import com.learning.weatherappclean.data.souce.remote.WeatherApi
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.ResourceDomain
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository
import javax.inject.Inject


class RemoteRepositoryImpl @Inject constructor(private val weatherApi: WeatherApi) :
    RemoteRepository, BaseRepository() {

    override suspend fun getWeatherData(request: Request): ResourceDomain<WeatherCard> {
        val response =
            safeApiCall { weatherApi.getWeather(city = request.query, units = request.units) }
        if (response is Resource.Error) return ResourceDomain.Error(
            errorType = response.type as ErrorType,
            errorMessage = response.message ?: "error",
            errorCode = null
        )
        /** The only way I found to distinct success response from failed one is to
        try to parse API response in WeatherResponse and if it fails, in ErrorResponse

        I have to handle errors on two levels, lower one (http and IO errors) and higher one,
        when I get API response with code 200 and OK message but there is error data instead of
        weather data in response body. This makes my code ugly
         */
        val weatherResponse = JsonConverter().convertFromJson<WeatherResponse>(response.data)
        return if (weatherResponse?.current != null) {
            ResourceDomain.Success(Mapper().mapToDomain(weatherResponse))
        } else {
            val errorResponse = JsonConverter().convertFromJson<ErrorResponse>(response.data)
            if (errorResponse != null) {
                ResourceDomain.Error(
                    errorType = ErrorType.API_ERROR,
                    errorMessage = errorResponse.error.info,
                    errorCode = errorResponse.error.code
                )
            } else ResourceDomain.Error(
                errorType = ErrorType.UNKNOWN_ERROR,
                errorMessage = response.message ?: "unknown error",
                errorCode = null
            )
        }
    }

    override suspend fun getAutocompletePredictions(request: Request): ResourceDomain<List<AutocompletePrediction>> {
        val response = safeApiCall { weatherApi.getAutocomplete(searchString = request.query) }
        if (response is Resource.Error) return ResourceDomain.Error(
            errorType = response.type as ErrorType,
            errorMessage = response.message ?: "error",
            errorCode = null
        )
        val autocompleteResponse =
            JsonConverter().convertFromJson<AutocompleteResponse>(response.data)
        return if (autocompleteResponse?.predictionData != null) {
            ResourceDomain.Success(data = Mapper().mapToDomain(autocompleteResponse))
        } else {
            val errorResponse = JsonConverter().convertFromJson<ErrorResponse>(response.data)
            if (errorResponse != null) {
                ResourceDomain.Error(
                    errorType = ErrorType.API_ERROR,
                    errorMessage = errorResponse.error.info,
                    errorCode = errorResponse.error.code
                )
            } else ResourceDomain.Error(
                errorType = ErrorType.UNKNOWN_ERROR,
                errorMessage = response.message ?: "unknown error",
                errorCode = null
            )
        }
    }
}



