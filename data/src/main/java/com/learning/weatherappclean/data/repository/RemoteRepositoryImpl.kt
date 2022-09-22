package com.learning.weatherappclean.data.repository


import android.util.Log
import com.learning.weatherappclean.data.model.apierror.internal.ErrorResponse
import com.learning.weatherappclean.data.model.autocompletedata.AutocompleteResponse
import com.learning.weatherappclean.data.util.Constants.API_KEY
import com.learning.weatherappclean.data.util.Constants.WEATHER_UNITS
import com.learning.weatherappclean.data.model.weatherdata.WeatherResponse
import com.learning.weatherappclean.data.souce.remote.BaseRepository
import com.learning.weatherappclean.data.souce.remote.Resource
import com.learning.weatherappclean.data.souce.remote.WeatherApi
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.Request
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository
import com.squareup.moshi.Moshi


class RemoteRepositoryImpl(private val weatherApi: WeatherApi) : RemoteRepository,BaseRepository() {
    //val m=Mapper()
    override suspend fun getWeatherData(request: Request): WeatherCard {
        Log.d("my_tag", "weather query")
        val response =
        safeApiCall {
            weatherApi.getWeather(
                accessKey = API_KEY,
                city = request.request,
                units = WEATHER_UNITS
            ) }

        val weatherResponse = Parser().convertBody<WeatherResponse>(response)
        return if (weatherResponse?.current != null) {
            //Log.d("my_tag", "resultWeather"+(weatherResponse.current ==null)+weatherResponse.toString())
            Mapper().mapToDomain(weatherResponse)
        } else {
            val resultError = Parser().convertBody<ErrorResponse>(response)
            //Log.d("my_tag", "resultError"+resultError.toString())
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



        val autocompleteResponse = Parser().convertBody<AutocompleteResponse>(response)
        Log.d("my_tag", "autocomplete after conversion $autocompleteResponse")
        return if (autocompleteResponse?.predictionData != null) {
            Mapper().mapToDomain(autocompleteResponse)
        } else {
            val errorResponse = Parser().convertBody<ErrorResponse>(response)
            Mapper().mapToDomain<AutocompletePrediction>(errorResponse!!) as AutocompletePrediction

        }

    }






}



