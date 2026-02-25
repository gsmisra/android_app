package com.dev.gauravmisra.happyfingers.tools



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun VisualTimer() {
    var seconds by remember { mutableStateOf(60) }
    var running by remember { mutableStateOf(false) }
    var remaining by remember { mutableStateOf(60) }

    LaunchedEffect(running, seconds) {
        if (!running) return@LaunchedEffect
        remaining = seconds
        while (running && remaining > 0) {
            delay(1000)
            remaining--
        }
        running = false
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Visual Timer", style = MaterialTheme.typography.titleLarge)

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AssistChip(onClick = { seconds = 30 }, label = { Text("30s") })
            AssistChip(onClick = { seconds = 60 }, label = { Text("1m") })
            AssistChip(onClick = { seconds = 120 }, label = { Text("2m") })
        }

        Box(Modifier.fillMaxWidth().height(260.dp)) {
            Canvas(Modifier.fillMaxSize()) {
                val frac = if (seconds == 0) 0f else remaining / seconds.toFloat()
                val r = size.minDimension * 0.35f
                val c = Offset(size.width / 2, size.height / 2)

                // background ring
                drawCircle(Color(0xFFE0E0E0), r, c)
                // remaining (shrinking)
                drawCircle(Color(0xFF7FB069).copy(alpha = 0.85f), r * frac, c)
            }
        }

        Text("Remaining: ${remaining}s")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { running = true }, enabled = !running) { Text("Start") }
            OutlinedButton(onClick = { running = false }, enabled = running) { Text("Stop") }
            OutlinedButton(onClick = { running = false; remaining = seconds }) { Text("Reset") }
        }
    }
}
