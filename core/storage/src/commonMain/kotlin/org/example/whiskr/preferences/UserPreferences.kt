package org.example.whiskr.preferences

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
class UserPreferences(private val settings: ObservableSettings) {

    val isDarkTheme: Flow<Boolean> = settings.getBooleanFlow(
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