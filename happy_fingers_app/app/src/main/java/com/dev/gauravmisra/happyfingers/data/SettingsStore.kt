package com.dev.gauravmisra.happyfingers.data


import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("happyfingers_settings")

class SettingsStore(private val context: Context) {

    private val LOW_STIM = booleanPreferencesKey("low_stim")
    private val SOUND_ON = booleanPreferencesKey("sound_on")
    private val VIBRATE_ON = booleanPreferencesKey("vibrate_on")
    private val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
    private val ACCESSIBILITY_MODE = booleanPreferencesKey("accessibility_mode")

    val settings: Flow<AppSettings> =
        context.dataStore.data.map { prefs ->
            AppSettings(
                lowStim = prefs[LOW_STIM] ?: true,
                soundOn = prefs[SOUND_ON] ?: false,
                vibrateOn = prefs[VIBRATE_ON] ?: false,
                highContrast = prefs[HIGH_CONTRAST] ?: false,
                accessibilityMode = prefs[ACCESSIBILITY_MODE] ?: false
            )
        }

    suspend fun setLowStim(value: Boolean) {
        context.dataStore.edit { it[LOW_STIM] = value }
    }

    suspend fun setSoundOn(value: Boolean) {
        context.dataStore.edit { it[SOUND_ON] = value }
    }

    suspend fun setVibrateOn(value: Boolean) {
        context.dataStore.edit { it[VIBRATE_ON] = value }
    }

    suspend fun setHighContrast(value: Boolean) {
        context.dataStore.edit { it[HIGH_CONTRAST] = value }
    }

    suspend fun setAccessibilityMode(value: Boolean) {
        context.dataStore.edit { it[ACCESSIBILITY_MODE] = value }
    }
}
