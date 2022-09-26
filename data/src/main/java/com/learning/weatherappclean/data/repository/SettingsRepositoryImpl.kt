package com.learning.weatherappclean.data.repository


import com.learning.weatherappclean.data.souce.local.SettingsStorage
import com.learning.weatherappclean.data.util.Mapper
import com.learning.weatherappclean.domain.model.Settings
import com.learning.weatherappclean.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val settingsStorage: SettingsStorage):SettingsRepository {

    override fun loadSettings(): Settings {
       return  Mapper().mapToDomain(settingsStorage.load())
    }

    override fun saveSettings(settings: Settings): Boolean {
        settingsStorage.save(settings)

        return true
    }




}