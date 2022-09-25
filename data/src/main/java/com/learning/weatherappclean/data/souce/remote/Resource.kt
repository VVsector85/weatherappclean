package com.learning.weatherappclean.data.souce.remote

import com.learning.weatherappclean.data.model.apierror.connection.ErrorType

sealed class Resource<T>(
    val data: T? = null,
    val type: ErrorType? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : Resource<T>(data = data)

    class Error<T>(errorType: ErrorType,errorMessage: String) : Resource<T>(type = errorType,message = errorMessage)

    // We'll just pass object of this Loading
    // class, just before making an api call
    class Loading<T> : Resource<T>()
}