package com.learning.weatherappclean.data.source.remote

import com.learning.weatherappclean.data.model.ErrorType

sealed class ResourceData<T>() {
    open val data: T? = null
    class Success<T>(override val data: T) : ResourceData<T>()
    class Error<T>(val errorType: ErrorType, val errorMessage: String) : ResourceData<T>()
}
