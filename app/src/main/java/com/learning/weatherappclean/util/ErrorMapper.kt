package com.learning.weatherappclean.util

import com.learning.weatherappclean.data.model.apierror.connection.ErrorType

class ErrorMapper {

    fun mapToPresentation(error: ErrorType): ErrorTypeUi {
        return when (error){
            ErrorType.UNKNOWN_ERROR -> ErrorTypeUi.UNKNOWN_ERROR
            ErrorType.API_ERROR -> ErrorTypeUi.API_ERROR
            ErrorType.IO_ERROR -> ErrorTypeUi.IO_ERROR
        else -> ErrorTypeUi.UNKNOWN_ERROR
        }
    }
}