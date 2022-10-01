package com.learning.weatherappclean.domain.model

data class Settings(
    var fahrenheit : Boolean = true,
    var newCardFirst:Boolean = true,
    var detailsOnDoubleTap:Boolean = true,
    var dragAndDropCards:Boolean = true

)