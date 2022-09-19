package com.learning.weatherappclean.data.repository


import android.util.Log
import com.learning.weatherappclean.data.model.apierror.ErrorResponse
import com.learning.weatherappclean.data.model.autocompletedata.AutocompleteResponse
import com.learning.weatherappclean.data.util.Constants.API_KEY
import com.learning.weatherappclean.data.util.Constants.WEATHER_UNITS
import com.learning.weatherappclean.data.model.weatherdata.WeatherResponse
import com.learning.weatherappclean.data.remote_source.WeatherApi
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository
import com.squareup.moshi.Moshi
import retrofit2.Response
import kotlin.reflect.KClass


class RemoteRepositoryImpl(private val weatherApi: WeatherApi) : RemoteRepository {

    override suspend fun getWeatherData(location: WeatherCard): WeatherCard {
        Log.d("my_tag", "weather query")
        val response =
            weatherApi.getWeather(
                accessKey = API_KEY,
                city = location.location,
                units = WEATHER_UNITS
            )
        val weatherResponse = convertBody<WeatherResponse>(response)
        return if (weatherResponse?.current != null) {
            //Log.d("my_tag", "resultWeather"+(weatherResponse.current ==null)+weatherResponse.toString())
            mapToDomain(weatherResponse)
        } else {
            val resultError = convertBody<ErrorResponse>(response)
            //Log.d("my_tag", "resultError"+resultError.toString())
            mapToDomain<WeatherCard>(resultError!!) as WeatherCard

        }


    }

    override suspend fun getAutocompletePredictions(searchString: AutocompletePrediction): AutocompletePrediction {
        val response = weatherApi.getAutocomplete(
            accessKey = API_KEY,
            searchString = searchString.searchString
        )
        val autocompleteResponse = convertBody<AutocompleteResponse>(response)
        Log.d("my_tag", "autocomplete after conversion $autocompleteResponse")
        return if (autocompleteResponse?.predictionData != null) {
            mapToDomain(autocompleteResponse)
        } else {
            val errorResponse = convertBody<ErrorResponse>(response)
            mapToDomain<AutocompletePrediction>(errorResponse!!) as AutocompletePrediction

        }

    }

    private fun mapToDomain(response: WeatherResponse): WeatherCard {
        return WeatherCard(
            location = response.location.name,
            temperature = response.current.temperature,
            cloudCover = response.current.cloudcover,
            feelsLike = response.current.feelslike,
            humidity = response.current.humidity,
            observationTime = response.current.observationTime,
            pressure = response.current.pressure,
            uvIndex = response.current.uvIndex,
            visibility = response.current.visibility,
            weatherCode = response.current.weatherCode,
            windDegree = response.current.windDegree,
            windDir = response.current.windDir,
            windSpeed = response.current.windSpeed,

            country = response.location.country,
            region = response.location.region,
            lat = response.location.lat,
            lon = response.location.lon,

            localtime = response.location.localtime,
            timezoneId = response.location.timezoneId,
            utcOffset = response.location.utcOffset,


            )
    }


    private inline fun <reified T> mapToDomain(response: ErrorResponse): Any? {
        return when (T::class) {
            WeatherCard::class -> WeatherCard(
                location = "none",
                error = true,
                errorMsg = "Error code: ${response.error.code}, ${response.error.type}, ${response.error.info}"
            )
            AutocompletePrediction::class -> AutocompletePrediction(

                error = true,
                errorMsg = "Error code: ${response.error.code}, ${response.error.type}, ${response.error.info}"
            )
            else -> null
        }
    }

    private fun mapToDomain(response: AutocompleteResponse): AutocompletePrediction {
        val tempList: MutableList<String> = mutableListOf()
        response.predictionData.forEach {
            Log.d("my_tag", "prediction item ${it.name}, ${it.country}")
            tempList.add("${it.name}, ${it.country}")
        }

        return AutocompletePrediction(
            searchString = response.request.query,
            predictions = tempList
        )
    }


    private inline fun <reified T> convertBody(response: Response<String>): T? {
        return try {
            Log.d("my_tag", "convert to error " + response.body())
            response.body().let {
                val moshiAdapter = Moshi.Builder().build().adapter(T::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }



}



