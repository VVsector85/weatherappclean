package com.learning.weatherappclean.domain.repository

import com.learning.weatherappclean.domain.model.Settings

interface SettingsRepository {
    fun saveSettings(settings: Settings): Boolean
    fun loadSettings(): Settings
}
