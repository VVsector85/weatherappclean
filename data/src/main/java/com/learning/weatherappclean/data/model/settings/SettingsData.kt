package com.learning.weatherappclean.data.model.settings

import com.squareup.moshi.Json

class SettingsData    (
    @field:Json( name ="fahrenheit")
    val fahrenheit : Boolean,
    @field:Json( name ="newCardFirst")
    val newCardFirst:Boolean,
    @field:Json( name ="showFeelsLike")
    val showFeelsLike:Boolean,
    @field:Json( name ="showCountry")
    val showCountry:Boolean

)



