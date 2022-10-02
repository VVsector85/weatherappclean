package com.learning.weatherappclean.domain.model

data class Request(
    val query: String,
    val location:String = "",
    val country:String = "",
    val region: String = "",
    val lon:String = "",
    val lat:String = "",
    val showDetails: Boolean = false,
    var units: String = "m"
)