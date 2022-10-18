package com.learning.weatherappclean.data.source.remote

import com.learning.weatherappclean.data.model.ErrorType

sealed class Resource<T>(
    val data: T? = null,
    val type: ErrorType? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(errorType: ErrorType, errorMessage: String) : Resource<T>(type = errorType, message = errorMessage)
}
