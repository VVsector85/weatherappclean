package com.learning.weatherappclean.data.model.apierror.connection

class ConnectionError(
    val errorType:ErrorType?=null,
    val errorMessage:String="",
    ) {
}
enum class ErrorType{

    HTTP_ERROR,
    IO_ERROR,
    INTERNAL_ERROR,
    UNKNOWN_ERROR
}