package com.dev.gauravmisra.happyfingers.ui.components


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.dev.gauravmisra.happyfingers.data.AppSettings
import com.dev.gauravmisra.happyfingers.data.*


@Composable
fun AccessibleFloatingHome(nav: NavController) {
    val context = LocalContext.current
    val store = remember { SettingsStore(context) }

    // ✅ IMPORTANT: provide a non-null initial value
    val settings by store.settings.collectAsState(
        initial = AppSettings()
    )

    if (settings.accessibilityMode) {
        FloatingHomeButton(nav = nav)
    }
}
