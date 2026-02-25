package com.dev.gauravmisra.happyfingers.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dev.gauravmisra.happyfingers.tools.FirstThenBoard
import com.dev.gauravmisra.happyfingers.tools.VisualSchedule
import com.dev.gauravmisra.happyfingers.tools.VisualTimer
import com.dev.gauravmisra.happyfingers.ui.components.AccessibleFloatingHome
import com.dev.gauravmisra.happyfingers.ui.components.AppTopBar
import com.dev.gauravmisra.happyfingers.ui.components.FloatingHomeButton

@Composable
fun MyDayScreen(nav: NavController) {
    var tab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = { AppTopBar(title = "My Day", nav = nav) },
        floatingActionButton = { AccessibleFloatingHome(nav = nav) }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = tab) {
                Tab(tab == 0, { tab = 0 }, text = { Text("Schedule") })
                Tab(tab == 1, { tab = 1 }, text = { Text("First–Then") })
                Tab(tab == 2, { tab = 2 }, text = { Text("Timer") })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when (tab) {
                    0 -> VisualSchedule()
                    1 -> FirstThenBoard()
                    2 -> VisualTimer()
                }
            }
        }
    }
}
