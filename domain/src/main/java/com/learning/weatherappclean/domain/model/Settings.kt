package com.learning.weatherappclean.domain.model

data class Settings(
    var fahrenheit : Boolean = true,
    var newCardFirst:Boolean = true,
    var showFeelsLike:Boolean = true,
    var showCountry:Boolean = true

)