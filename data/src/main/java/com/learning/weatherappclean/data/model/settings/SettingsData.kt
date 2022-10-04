package com.learning.weatherappclean.data.model.settings

import com.squareup.moshi.Json

class SettingsData    (
    @field:Json( name ="imperialUnits")
    val imperialUnits : Boolean,
    @field:Json( name ="newCardFirst")
    val newCardFirst:Boolean,
    @field:Json( name ="detailsOnDoubleTap")
    val detailsOnDoubleTap:Boolean,
    @field:Json( name ="dragAndDropCards")
    val dragAndDropCards:Boolean
)



