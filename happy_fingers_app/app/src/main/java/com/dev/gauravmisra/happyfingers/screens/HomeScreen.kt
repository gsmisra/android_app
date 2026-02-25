package com.dev.gauravmisra.happyfingers.screens



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dev.gauravmisra.happyfingers.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HappyFingers") },
                actions = {
                    TextButton(onClick = { nav.navigate(Routes.SETTINGS) }) { Text("Settings") }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Choose a zone", style = MaterialTheme.typography.headlineSmall)

            Button(modifier = Modifier.fillMaxWidth(), onClick = { nav.navigate(Routes.CALM) }) {
                Text("Calm")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { nav.navigate(Routes.PLAY) }) {
                Text("Play")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { nav.navigate(Routes.TALK) }) {
                Text("Talk")
            }
            Button(modifier = Modifier.fillMaxWidth(), onClick = { nav.navigate(Routes.MYDAY) }) {
                Text("My Day")
            }

            Spacer(Modifier.height(12.dp))
            Text(
                "Tip: Start with Calm → then Play/Talk.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
