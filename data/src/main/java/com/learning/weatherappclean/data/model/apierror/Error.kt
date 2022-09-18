package com.learning.weatherappclean.data.model.apierror


import com.google.gson.annotations.SerializedName


data class Error(
    @SerializedName( "code")
    val code: Int,
    @SerializedName( "info")
    val info: String,
    @SerializedName( "type")
    val type: String
)