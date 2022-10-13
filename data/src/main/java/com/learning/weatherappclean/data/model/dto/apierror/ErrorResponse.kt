package com.learning.weatherappclean.data.model.dto.apierror

import com.squareup.moshi.Json


data class ErrorResponse(
    @field:Json( name ="error")
    val error: Error,
    @field:Json( name = "success")
    val success: Boolean
)