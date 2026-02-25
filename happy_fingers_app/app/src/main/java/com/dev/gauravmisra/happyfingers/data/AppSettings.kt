package com.dev.gauravmisra.happyfingers.data



data class AppSettings(
    val lowStim: Boolean = true,
    val soundOn: Boolean = false,
    val vibrateOn: Boolean = false,
    val highContrast: Boolean = false,
    val accessibilityMode: Boolean = false
)
