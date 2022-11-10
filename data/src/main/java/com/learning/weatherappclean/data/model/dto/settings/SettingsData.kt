package com.learning.weatherappclean.data.model.dto.settings

import com.learning.weatherappclean.domain.model.Settings

data class SettingsData(

    val imperialUnits: Boolean,
    val newCardFirst: Boolean,
    val detailsOnDoubleTap: Boolean,
    val dragAndDropCards: Boolean,
    val showVideo: Boolean,
    val swipeToDismiss: Boolean
)

internal fun SettingsData.mapToDomain(): Settings =
    Settings(
        imperialUnits = imperialUnits,
        newCardFirst = newCardFirst,
        detailsOnDoubleTap = detailsOnDoubleTap,
        dragAndDropCards = dragAndDropCards,
        showVideo = showVideo,
        swipeToDismiss = swipeToDismiss
    )
