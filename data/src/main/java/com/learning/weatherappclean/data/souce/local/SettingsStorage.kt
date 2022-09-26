package com.learning.weatherappclean.data.souce.local

import com.learning.weatherappclean.data.model.settings.SettingsData
import com.learning.weatherappclean.domain.model.Settings


interface SettingsStorage{
        fun save(settings:Settings): Boolean
        fun load(): SettingsData
    }
