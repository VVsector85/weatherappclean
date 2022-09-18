package com.learning.weatherappclean.data.model.apierror

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json


data class ErrorResponse(
    @field:Json( name ="error")
    val error: Error,
    @field:Json( name = "success")
    val success: Boolean
)