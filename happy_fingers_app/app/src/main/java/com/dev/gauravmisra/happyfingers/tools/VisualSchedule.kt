package com.dev.gauravmisra.happyfingers.tools



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dev.gauravmisra.happyfingers.data.*

@Composable
fun VisualSchedule() {
    var steps by remember {
        mutableStateOf(
            listOf(
                ScheduleStep("1", "Brush teeth"),
                ScheduleStep("2", "Get dressed"),
                ScheduleStep("3", "Breakfast"),
                ScheduleStep("4", "School / Play"),
            )
        )
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Visual Schedule", style = MaterialTheme.typography.titleLarge)

        steps.forEachIndexed { index, step ->
            Card {
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${index + 1}. ${step.label}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { if (index > 0) steps = steps.toMutableList().apply { add(index - 1, removeAt(index)) } },
                            enabled = index > 0
                        ) { Text("↑") }

                        OutlinedButton(
                            onClick = { if (index < steps.lastIndex) steps = steps.toMutableList().apply { add(index + 1, removeAt(index)) } },
                            enabled = index < steps.lastIndex
                        ) { Text("↓") }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Tip: keep steps short and predictable.")
    }
}
