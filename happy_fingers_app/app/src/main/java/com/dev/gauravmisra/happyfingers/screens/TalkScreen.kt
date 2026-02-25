package com.dev.gauravmisra.happyfingers.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dev.gauravmisra.happyfingers.tools.ChoiceBoardTTS
import com.dev.gauravmisra.happyfingers.tools.EmotionPicker
import com.dev.gauravmisra.happyfingers.ui.components.AccessibleFloatingHome
import com.dev.gauravmisra.happyfingers.ui.components.AppTopBar
import com.dev.gauravmisra.happyfingers.ui.components.FloatingHomeButton

@Composable
fun TalkScreen(nav: NavController) {
    var tab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = { AppTopBar(title = "Talk", nav = nav) },
        floatingActionButton = { AccessibleFloatingHome(nav = nav) }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = tab) {
                Tab(tab == 0, { tab = 0 }, text = { Text("Choices") })
                Tab(tab == 1, { tab = 1 }, text = { Text("Feelings") })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when (tab) {
                    0 -> ChoiceBoardTTS()
                    1 -> EmotionPicker()
                }
            }
        }
    }
}
