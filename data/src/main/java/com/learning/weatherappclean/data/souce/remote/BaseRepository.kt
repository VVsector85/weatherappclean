package com.learning.weatherappclean.data.souce.remote

import android.util.Log
import com.learning.weatherappclean.data.model.apierror.connection.ConnectionError
import com.learning.weatherappclean.data.model.apierror.connection.ErrorType
import com.learning.weatherappclean.data.model.apierror.internal.ErrorResponse
import com.squareup.moshi.Moshi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException



/**"https://www.geeksforgeeks.org/how-to-handle-api-responses-success-error-in-android/"*/
abstract class BaseRepository() {

    // we'll use this function in all
    // repos to handle api errors.
    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): Resource<T> {

        // Returning api response
        // wrapped in Resource class
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()
                if (response.isSuccessful) {
                   Resource.Success(data = response.body()!!)
                } else {
                    val errorResponse: ErrorResponse? = convertErrorBody(response.errorBody())
                    Resource.Error(errorMessage = errorResponse?.error?.info ?: "Something went wrong", errorType = ErrorType.INTERNAL_ERROR)
                }

            } catch (e: HttpException) {
                Resource.Error(errorMessage = e.message ?: "Something went wrong",errorType = ErrorType.HTTP_ERROR)
            } catch (e: IOException) {
                Resource.Error(errorMessage = e.message ?:"Please check your network connection", errorType = ErrorType.IO_ERROR)
            } catch (e: Exception) {
                Resource.Error(errorMessage = e.message ?: "Something went wrong", errorType = ErrorType.UNKNOWN_ERROR)
            }
        }
    }

    // If you don't wanna handle api's own
    // custom error response then ignore this function
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
