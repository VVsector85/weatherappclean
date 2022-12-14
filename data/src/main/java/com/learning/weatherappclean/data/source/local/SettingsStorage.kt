package com.learning.weatherappclean.data.source.local

import com.learning.weatherappclean.data.model.dto.settings.SettingsData
import com.learning.weatherappclean.domain.model.Settings

interface SettingsStorage {
    fun save(settings: Settings): Boolean
    fun load(): SettingsData
}
