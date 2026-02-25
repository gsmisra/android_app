package com.dev.gauravmisra.happyfingers.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dev.gauravmisra.happyfingers.games.calm.BubbleCalm
import com.dev.gauravmisra.happyfingers.games.calm.GlitterJar
import com.dev.gauravmisra.happyfingers.ui.components.AccessibleFloatingHome
import com.dev.gauravmisra.happyfingers.ui.components.AppTopBar
import com.dev.gauravmisra.happyfingers.ui.components.FloatingHomeButton


@Composable
fun CalmScreen(nav: NavController) {
    var tab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = { AppTopBar(title = "Calm", nav = nav) },
        floatingActionButton = { AccessibleFloatingHome(nav = nav) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = tab) {
                Tab(tab == 0, { tab = 0 }, text = { Text("Bubbles") })
                Tab(tab == 1, { tab = 1 }, text = { Text("Glitter Jar") })
            }

            Spacer(Modifier.height(8.dp))

            Box(Modifier.fillMaxSize()) {
                when (tab) {
                    0 -> BubbleCalm()
                    1 -> GlitterJar()
                }
            }
        }
    }
}

