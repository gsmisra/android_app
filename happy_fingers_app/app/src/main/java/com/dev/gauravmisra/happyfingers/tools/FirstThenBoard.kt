package com.dev.gauravmisra.happyfingers.tools


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FirstThenBoard() {
    val options = listOf("Puzzle", "Clean up", "Snack", "Bubbles", "Play", "Story")
    var first by remember { mutableStateOf(options[0]) }
    var then by remember { mutableStateOf(options[3]) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("First – Then", style = MaterialTheme.typography.titleLarge)

        Card {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("First: $first", style = MaterialTheme.typography.titleMedium)
                Text("Then:  $then", style = MaterialTheme.typography.titleMedium)
            }
        }

        Text("Pick First")
        FlowRowChips(options, selected = first) { first = it }

        Text("Pick Then")
        FlowRowChips(options, selected = then) { then = it }
    }
}

@Composable
private fun FlowRowChips(options: List<String>, selected: String, onPick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.chunked(3).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { opt ->
                    AssistChip(
                        onClick = { onPick(opt) },
                        label = { Text(opt) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (opt == selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        }
    }
}
