package com.dev.gauravmisra.happyfingers.games.play



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.random.Random

private data class Flower(val p: Offset, val petals: Int, val hue: Float)

@Composable
fun CauseEffectGarden() {
    val flowers = remember { mutableStateListOf<Flower>() }

    Canvas(
        Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures { tap ->
                flowers.add(
                    Flower(
                        p = tap,
                        petals = Random.nextInt(5, 10),
                        hue = Random.nextFloat()
                    )
                )
                if (flowers.size > 60) flowers.removeAt(0)
            }
        }
    ) {
        drawRect(Color(0xFFEFF8E7))
        // ground
        drawRect(Color(0xFFB7E4C7), topLeft = Offset(0f, size.height * 0.78f),
            size = androidx.compose.ui.geometry.Size(size.width, size.height * 0.22f))

        flowers.forEach { f ->
            val base = Color.hsv(f.hue * 360f, 0.55f, 0.95f)
            val center = f.p
            // stem
            drawLine(Color(0xFF2D6A4F), center + Offset(0f, 25f), center + Offset(0f, 90f), 8f)
            // petals
            repeat(f.petals) { i ->
                val angle = (i * (360f / f.petals)) * (Math.PI / 180f).toFloat()
                val dx = kotlin.math.cos(angle) * 22f
                val dy = kotlin.math.sin(angle) * 22f
                drawCircle(base.copy(alpha = 0.9f), 16f, center + Offset(dx, dy))
            }
            // center
            drawCircle(Color(0xFFFFE169), 12f, center)
        }
    }
}
