package com.learning.weatherappclean.data.model.dto.apierror

data class Error(
    val code: Int,
    val info: String,
    val type: String
)