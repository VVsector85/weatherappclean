package com.learning.weatherappclean.domain.model

   sealed class ResourceDomain<T>(
        val data: T? = null,
        val type: Any? = null,
        val code: Int? = null,
        val message: String? = null
    ) {

        class Success<T>(data: T) : ResourceDomain<T>(data = data)
        class Error<T>(errorType: Any,errorMessage: String, errorCode:Int?) : ResourceDomain<T>(type = errorType,message = errorMessage,code = errorCode)

    }
