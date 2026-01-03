package org.example.whiskr.preferences

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings

@OptIn(ExperimentalSettingsApi::class)
class UserPreferences {
    private val settings = Settings()

    val isDarkTheme: Boolean = settings.getBoolean(
        key = KEY_IS_DARK_THEME,
        defaultValue = false
    )

    fun setDarkTheme(isDark: Boolean) {
        settings.putBoolean(KEY_IS_DARK_THEME, isDark)
    }

    companion object {
        private const val KEY_IS_DARK_THEME = "is_dark_theme"
    }
}