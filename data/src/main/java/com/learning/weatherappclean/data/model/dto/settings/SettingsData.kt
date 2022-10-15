package com.learning.weatherappclean.data.model.dto.settings

import com.learning.weatherappclean.domain.model.Settings

class SettingsData    (

    val imperialUnits : Boolean,
    val newCardFirst:Boolean,
    val detailsOnDoubleTap:Boolean,
    val dragAndDropCards:Boolean
)


internal fun SettingsData.mapToDomain(): Settings =
    Settings(
        imperialUnits = this.imperialUnits,
        newCardFirst = this.newCardFirst,
        detailsOnDoubleTap = this.detailsOnDoubleTap,
        dragAndDropCards = this.dragAndDropCards
    )
