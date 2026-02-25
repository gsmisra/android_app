package com.dev.gauravmisra.the_offencive_joker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.dev.gauravmisra.the_offencive_joker.ui.theme.Typography

// ui/JokerTheme.kt
@Composable
fun JokerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFFFEB3B),
            secondary = Color(0xFF4CAF50),
            background = Color(0xFF121212)
        ),
        typography = Typography(),
        content = content
    )
}