package com.dev.gauravmisra.happyfingers.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dev.gauravmisra.happyfingers.games.play.CauseEffectGarden
import com.dev.gauravmisra.happyfingers.games.play.MatchingShadow
import com.dev.gauravmisra.happyfingers.games.play.TracingPath
import com.dev.gauravmisra.happyfingers.ui.components.AccessibleFloatingHome
import com.dev.gauravmisra.happyfingers.ui.components.AppTopBar
import com.dev.gauravmisra.happyfingers.ui.components.FloatingHomeButton

@Composable
fun PlayScreen(nav: NavController) {
    var tab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = { AppTopBar(title = "Play", nav = nav) },

        floatingActionButton = { AccessibleFloatingHome(nav = nav) }


    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = tab) {
                Tab(tab == 0, { tab = 0 }, text = { Text("Garden") })
                Tab(tab == 1, { tab = 1 }, text = { Text("Match") })
                Tab(tab == 2, { tab = 2 }, text = { Text("Trace") })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when (tab) {
                    0 -> CauseEffectGarden()
                    1 -> MatchingShadow()
                    2 -> TracingPath()
                }
            }
        }
    }
}
