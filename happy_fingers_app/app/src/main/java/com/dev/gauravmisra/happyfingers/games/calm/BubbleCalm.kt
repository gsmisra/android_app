package com.dev.gauravmisra.happyfingers.games.calm


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.random.Random

private data class Bubble(
    val id: Long,
    var pos: Offset,
    var radius: Float,
    var vy: Float,
    val color: Color
)

@Composable
fun BubbleCalm() {
    val bubbles = remember { mutableStateListOf<Bubble>() }

    // animation loop
    LaunchedEffect(Unit) {
        while (true) {
            for (b in bubbles) {
                b.pos = b.pos.copy(y = b.pos.y - b.vy)
                b.vy = max(0.5f, b.vy * 0.995f) // slow drift
                b.radius *= 0.9995f
            }
            bubbles.removeAll { it.pos.y + it.radius < -50f || it.radius < 8f }
            delay(16)
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { tap ->
                    // pop if tapping inside a bubble
                    val hit = bubbles.lastOrNull { (it.pos - tap).getDistance() <= it.radius }
                    if (hit != null) {
                        bubbles.remove(hit)
                    } else {
                        bubbles.add(
                            Bubble(
                                id = System.nanoTime(),
                                pos = tap,
                                radius = Random.nextInt(30, 90).toFloat(),
                                vy = Random.nextDouble(0.8, 2.5).toFloat(),
                                color = Color(
                                    0xFF000000 or Random.nextLong(0x00FFFFFF)
                                ).copy(alpha = 0.25f)
                            )
                        )
                    }
                }
            }
    ) {
        drawRect(Color(0xFFF3FBFF))
        for (b in bubbles) {
            drawCircle(color = b.color, radius = b.radius, center = b.pos)
            drawCircle(
                color = Color.White.copy(alpha = 0.18f),
                radius = b.radius * 0.35f,
                center = b.pos + Offset(b.radius * 0.25f, -b.radius * 0.25f)
            )
        }
    }
}
