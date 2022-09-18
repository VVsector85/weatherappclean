package com.learning.weatherappclean.data.repository


import android.gesture.Prediction
import android.util.Log
import com.learning.weatherappclean.data.model.apierror.ErrorResponse
import com.learning.weatherappclean.data.model.autocompletedata.PredictionData
import com.learning.weatherappclean.data.model.autocompletedata.ResultAutocomplete
import com.learning.weatherappclean.data.util.Constants.API_KEY
import com.learning.weatherappclean.data.util.Constants.WEATHER_UNITS
import com.learning.weatherappclean.data.model.weatherdata.Weather
import com.learning.weatherappclean.data.remote_source.BaseRepository
import com.learning.weatherappclean.data.remote_source.Resource
import com.learning.weatherappclean.data.remote_source.WeatherApi
import com.learning.weatherappclean.domain.model.AutocompletePrediction
import com.learning.weatherappclean.domain.model.WeatherCard
import com.learning.weatherappclean.domain.repository.RemoteRepository
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Response


class RemoteRepositoryImpl(private val weatherApi: WeatherApi) : RemoteRepository {

    override suspend fun getWeatherData(location: WeatherCard): WeatherCard {
        Log.d("my_tag", "weather query")
        val response =
            weatherApi.getWeather(
                accessKey = API_KEY,
                city = location.location,
                units = WEATHER_UNITS
            )
        val resultWeather = convertBodyToWeather(response)
        return if (resultWeather?.current!=null){
            Log.d("my_tag", "resultWeather"+(resultWeather.current ==null)+resultWeather.toString())
            mapWeatherDataToDomain(resultWeather)
        } else {
            val resultError = convertBodyToError(response)
            Log.d("my_tag", "resultError"+resultError.toString())
            mapWeatherDataToDomain(resultError!!)
        }


    }

    override suspend fun getAutocompletePredictions(searchString: AutocompletePrediction): AutocompletePrediction {
        val response = weatherApi.getAutocomplete(
            accessKey = API_KEY,
            searchString = searchString.searchString
        )
        val resultAutocomplete = convertBodyToAutocomplete(response)
        Log.d("my_tag", "autocomplete after conversion $resultAutocomplete")
        return if (resultAutocomplete?.predictionData != null) {
           mapAutocompletePredictionDataToDomain(resultAutocomplete)
        } else {
            val resultError = convertBodyToError(response)
            mapAutocompletePredictionDataToDomain(resultError!!)
        }
    }

    private fun mapWeatherDataToDomain(response: Weather): WeatherCard {
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

    private fun mapWeatherDataToDomain(response: ErrorResponse): WeatherCard {
        return WeatherCard(
            location = "none",
            error = true,
            errorMsg = "Error code: ${response.error.code}, ${response.error.type}, ${response.error.info}"
        )
    }

    private fun mapAutocompletePredictionDataToDomain(response: ResultAutocomplete): AutocompletePrediction {
        val tempList: MutableList<String> = mutableListOf()
        response.predictionData.forEach {
           Log.d("my_tag","prediction item ${it.name}, ${it.country}")
            tempList.add("${it.name}, ${it.country}")
        }

        return AutocompletePrediction(
            searchString = response.request.query,
            predictions = tempList
        )
    }

    private fun mapAutocompletePredictionDataToDomain(response: ErrorResponse): AutocompletePrediction {
        return AutocompletePrediction(

            error = true,
            errorMsg = "Error code: ${response.error.code}, ${response.error.type}, ${response.error.info}"
        )
    }


    private fun convertBodyToAutocomplete(response: Response<String>): ResultAutocomplete? {
        return try {
            Log.d("my_tag", "convert to autocomplete "+response.body())
            response.body().let {
                val moshiAdapter =
                    Moshi.Builder().build().adapter(ResultAutocomplete::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }

    private fun convertBodyToWeather(response: Response<String>): Weather? {
        return try {
            Log.d("my_tag", "convert to weather "+response.body())
            response.body().let {
                val moshiAdapter = Moshi.Builder().build().adapter(Weather::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }

    /*private fun convertBodyToWeather(response: Response<String>): Weather? {
        val moshiAdapter = Moshi.Builder().build().adapter(Weather::class.java)
        return moshiAdapter.fromJson(response.body())

    }*/







    private fun convertBodyToError(response: Response<String>): ErrorResponse? {
        return try {
            Log.d("my_tag", "convert to error "+response.body())
            response.body().let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}



