package com.learning.weatherappclean.data.model.apierror

import com.google.gson.annotations.SerializedName


data class ErrorResponse(
    @SerializedName( "error")
    val error: Error,
    @SerializedName( "success")
    val success: Boolean
)