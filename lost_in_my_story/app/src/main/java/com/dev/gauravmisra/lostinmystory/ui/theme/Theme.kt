package com.dev.gauravmisra.lostinmystory.ui.theme


import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat



private val DarkColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = Midnight,

    secondary = Accent2,
    onSecondary = Midnight,

    background = Midnight,
    onBackground = TextPrimary,

    surface = SurfaceDark,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceElev,
    onSurfaceVariant = TextMuted,

    error = Danger,
    onError = Midnight
)

@Composable
fun OneMoreChoiceTheme(
    content: @Composable () -> Unit
) {
    // ✅ Always dark theme (no system-based switching)
    // ✅ Dynamic color is NOT used, so wallpaper colors won't override your palette. [3](https://dev.to/paulallies/clean-architecture-in-the-flavour-of-jetpack-compose-1j1)
    val colorScheme = DarkColorScheme

    // Optional: status bar styling
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
