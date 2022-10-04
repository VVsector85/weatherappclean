package com.learning.weatherappclean.domain.model

data class Settings(
    var imperialUnits : Boolean = true,
    var newCardFirst:Boolean = true,
    var detailsOnDoubleTap:Boolean = true,
    var dragAndDropCards:Boolean = true

)