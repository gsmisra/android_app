package com.dev.gauravmisra.happyfingers.ui.theme



import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Light = lightColorScheme(
    primary = Color(0xFF4F7CAC),
    secondary = Color(0xFF7FB069),
    background = Color(0xFFF7F7F7),
    surface = Color(0xFFFFFFFF)
)

private val Dark = darkColorScheme(
    primary = Color(0xFF8AB4F8),
    secondary = Color(0xFF81C995),
    background = Color(0xFF101114),
    surface = Color(0xFF1B1C20)
)

@Composable
fun NeuroPlayTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Light, content = content)
}
