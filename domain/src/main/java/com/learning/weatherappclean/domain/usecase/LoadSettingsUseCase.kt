package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.repository.SettingsRepository

class LoadSettingsUseCase(private val settingsRepository: SettingsRepository) {
        fun execute():Settings {
            return settingsRepository.loadSettings()
        }
    }





