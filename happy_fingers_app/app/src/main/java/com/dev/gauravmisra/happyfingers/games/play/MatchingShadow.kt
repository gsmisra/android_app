package com.dev.gauravmisra.happyfingers.games.play



import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.hypot

private data class Token(val id: Int, val label: String, val color: Color, var pos: Offset)
private data class Target(val id: Int, val label: String, val pos: Offset)

@Composable
fun MatchingShadow() {
    // simple 3-item match for MVP
    val tokens = remember {
        mutableStateListOf(
            Token(1, "Circle", Color(0xFF8AB4F8), Offset(100f, 200f)),
            Token(2, "Square", Color(0xFF81C995), Offset(100f, 360f)),
            Token(3, "Triangle", Color(0xFFF28B82), Offset(100f, 520f)),
        )
    }
    val targets = remember {
        listOf(
            Target(1, "Circle", Offset(750f, 220f)),
            Target(2, "Square", Offset(750f, 380f)),
            Target(3, "Triangle", Offset(750f, 540f)),
        )
    }
    var done by remember { mutableStateOf(setOf<Int>()) }

    Box(Modifier.fillMaxSize().background(Color(0xFFF7F7F7))) {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Drag to match", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(6.dp))
            Box(Modifier.fillMaxSize()) {

                // Targets (shadows)
                targets.forEach { t ->
                    Box(
                        Modifier
                            .offset((t.pos.x / 3).dp, (t.pos.y / 3).dp)
                            .size(90.dp)
                            .background(Color(0xFF000000).copy(alpha = 0.08f))
                    )
                    Text(
                        t.label,
                        modifier = Modifier.offset((t.pos.x / 3).dp, ((t.pos.y + 100f) / 3).dp),
                        color = Color(0xFF6B6B6B)
                    )
                }

                // Tokens
                tokens.forEach { token ->
                    if (done.contains(token.id)) return@forEach

                    Card(
                        modifier = Modifier
                            .offset((token.pos.x / 3).dp, (token.pos.y / 3).dp)
                            .size(90.dp)
                            .pointerInput(token.id) {
                                detectDragGestures { change, drag ->
                                    change.consume()
                                    token.pos = token.pos + drag

                                    val target = targets.first { it.id == token.id }
                                    val dist = hypot(token.pos.x - target.pos.x, token.pos.y - target.pos.y)
                                    if (dist < 40f) {
                                        done = done + token.id
                                    }
                                }
                            }
                    ) {
                        Box(Modifier.fillMaxSize().background(token.color.copy(alpha = 0.85f))) {
                            Text(token.label, modifier = Modifier.padding(10.dp), color = Color.White)
                        }
                    }
                }
            }
        }

        if (done.size == 3) {
            Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Card { Text("Great matching!", modifier = Modifier.padding(18.dp)) }
            }
        }
    }
}
