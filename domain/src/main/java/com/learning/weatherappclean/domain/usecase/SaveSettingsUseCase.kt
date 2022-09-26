package com.learning.weatherappclean.domain.usecase

import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.repository.SettingsRepository

class SaveSettingsUseCase  (private val settingsRepository: SettingsRepository){

    fun execute(settings: Settings):Boolean {
        return settingsRepository.saveSettings(settings =  settings)
    }
}