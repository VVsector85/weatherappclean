package com.learning.weatherappclean.data.repository

import com.learning.weatherappclean.data.model.ErrorType
import com.learning.weatherappclean.data.model.dto.apierror.ErrorResponse
import com.learning.weatherappclean.data.source.remote.Resource
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**"https://www.geeksforgeeks.org/how-to-handle-api-responses-success-error-in-android/"*/
abstract class BaseRepository() {
    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()

                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
                } else {
                    /** This never happens because WeatherStack API always
                     responds with code 200 and "OK" message, so response.isSuccessful = true
                     even in case of error*/
                    val errorResponse: ErrorResponse? = convertErrorBody(response.errorBody())
                    Resource.Error(
                        errorMessage = errorResponse?.error?.info ?: "",
                        errorType = ErrorType.INTERNAL_ERROR
                    )
                }
            } catch (e: HttpException) {
                Resource.Error(
                    errorMessage = e.message ?: "",
                    errorType = ErrorType.HTTP_ERROR
                )
            } catch (e: IOException) {
                Resource.Error(
                    errorMessage = e.message ?: "",
                    errorType = ErrorType.IO_ERROR
                )
            } catch (e: Exception) {
                Resource.Error(
                    errorMessage = e.message ?: "",
                    errorType = ErrorType.UNKNOWN_ERROR
                )
            }
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorResponse? {
        return try {
            errorBody?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}
