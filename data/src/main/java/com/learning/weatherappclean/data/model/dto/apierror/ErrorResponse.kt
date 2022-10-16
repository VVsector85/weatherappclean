package com.learning.weatherappclean.data.model.dto.apierror

data class ErrorResponse(
    val error: Error,
    val success: Boolean
)
