package com.dev.gauravmisra.happyfingers.tools



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmotionPicker() {
    var selected by remember { mutableStateOf<String?>(null) }

    val emotions = listOf("Happy 😊", "Sad 😢", "Angry 😠", "Tired 😴", "Worried 😟")

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("How do you feel?", style = MaterialTheme.typography.titleLarge)

        emotions.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { e ->
                    ElevatedCard(
                        modifier = Modifier.weight(1f).height(72.dp),
                        onClick = { selected = e }
                    ) { Box(Modifier.fillMaxSize().padding(12.dp)) { Text(e) } }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        if (selected != null) {
            Spacer(Modifier.height(12.dp))
            Card {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("You picked: $selected")
                    Text("Try one of these:")
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = { /* could navigate to Calm tab */ }) { Text("Bubbles") }
                        Button(onClick = { /* start timer */ }) { Text("2‑min Timer") }
                    }
                }
            }
        }
    }
}
