package com.dev.gauravmisra.happyfingers.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dev.gauravmisra.happyfingers.data.SettingsStore
import com.dev.gauravmisra.happyfingers.ui.components.AccessibleFloatingHome
import com.dev.gauravmisra.happyfingers.ui.components.AppTopBar
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(nav: NavController) {
    val ctx = LocalContext.current
    val store = remember { SettingsStore(ctx) }
    val scope = rememberCoroutineScope()
    val settings by store.settings.collectAsState(initial = null)

    Scaffold(
        topBar = { AppTopBar(title = "Settings", nav = nav) },
        floatingActionButton = { AccessibleFloatingHome(nav = nav) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val s = settings
            if (s == null) {
                CircularProgressIndicator()
                return@Column
            }

            // ✅ NEW: Accessibility toggle
            SettingToggle(
                label = "Accessibility Mode (large Home button)",
                value = s.accessibilityMode
            ) {
                scope.launch { store.setAccessibilityMode(it) }
            }

            SettingToggle("Low‑stim mode (reduce motion)", s.lowStim) {
                scope.launch { store.setLowStim(it) }
            }

            SettingToggle("Sound on", s.soundOn) {
                scope.launch { store.setSoundOn(it) }
            }

            SettingToggle("Vibrate on", s.vibrateOn) {
                scope.launch { store.setVibrateOn(it) }
            }

            SettingToggle("High contrast", s.highContrast) {
                scope.launch { store.setHighContrast(it) }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Tip: For young children, low‑stim + sound off is usually best.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SettingToggle(
    label: String,
    value: Boolean,
    onChange: (Boolean) -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label)
            Switch(
                checked = value,
                onCheckedChange = onChange
            )
        }
    }
}

