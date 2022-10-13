package com.learning.weatherappclean.data.souce.local.sharedprefs

import android.content.Context
import com.learning.weatherappclean.data.model.dto.settings.SettingsData
import com.learning.weatherappclean.data.souce.local.SettingsStorage
import com.learning.weatherappclean.data.util.JsonConverter
import com.learning.weatherappclean.domain.model.Settings

class SharedPrefsSettingsStorage(context: Context) : SettingsStorage {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_SETTINGS, Context.MODE_PRIVATE)

    override fun load(): SettingsData {
        return JsonConverter().convertFromJson<SettingsData>(
            jsonString = sharedPreferences.getString(
                KEY_SETTINGS, DEFAULT_SETTINGS
            ) ?: DEFAULT_SETTINGS
        )!!
    }

    override fun save(settings: Settings): Boolean {
        sharedPreferences.edit().putString(KEY_SETTINGS, JsonConverter().convertToJson(settings))
            .apply()
        return true
    }

    companion object {
        private const val SHARED_PREFS_SETTINGS = "shared_refs_weather_settings"
        private const val KEY_SETTINGS = "settings"
        private const val DEFAULT_SETTINGS =
            """{"imperialUnits":false,"newCardFirst":true,"detailsOnDoubleTap":true,"dragAndDropCards":true }"""
    }
}