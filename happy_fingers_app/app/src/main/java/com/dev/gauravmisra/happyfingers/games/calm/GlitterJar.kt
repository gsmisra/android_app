package com.dev.gauravmisra.happyfingers.games.calm



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlin.random.Random

private data class Particle(var p: Offset, var v: Offset, val c: Color, val r: Float)

@Composable
fun GlitterJar() {
    val particles = remember {
        mutableStateListOf<Particle>().apply {
            repeat(120) {
                add(
                    Particle(
                        p = Offset(Random.nextFloat() * 1000f, Random.nextFloat() * 1600f),
                        v = Offset(0f, 0f),
                        c = listOf(
                            Color(0xFFFFC857), Color(0xFF8AB4F8),
                            Color(0xFF81C995), Color(0xFFF28B82)
                        ).random().copy(alpha = 0.75f),
                        r = Random.nextInt(3, 8).toFloat()
                    )
                )
            }
        }
    }

    var swirl by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(Unit) {
        while (true) {
            for (pt in particles) {
                val v = pt.v + swirl * 0.03f + Offset(0f, 0.35f) // gravity
                pt.v = v * 0.96f
                pt.p = pt.p + pt.v

                // simple bounds wrap
                if (pt.p.y > 1800f) pt.p = pt.p.copy(y = -50f)
                if (pt.p.x > 1100f) pt.p = pt.p.copy(x = -50f)
                if (pt.p.x < -50f) pt.p = pt.p.copy(x = 1100f)
            }
            swirl *= 0.88f
            delay(16)
        }
    }

    Canvas(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, delta -> swirl = swirl + delta }
                )
            }
    ) {
        drawRect(Color(0xFF0B1B2B)) // dark calming backdrop
        // jar body
        val center = Offset(size.width / 2, size.height / 2)
        val jarW = size.width * 0.75f
        val jarH = size.height * 0.80f
        val topLeft = Offset(center.x - jarW / 2, center.y - jarH / 2)
        drawRoundRect(
            color = Color(0xFF102C44),
            topLeft = topLeft,
            size = androidx.compose.ui.geometry.Size(jarW, jarH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(60f, 60f)
        )
        // particles
        for (pt in particles) {
            drawCircle(pt.c, pt.r, center = Offset(
                topLeft.x + (pt.p.x % jarW),
                topLeft.y + (pt.p.y % jarH)
            ))
        }
        // glass highlight
        drawRoundRect(
            color = Color.White.copy(alpha = 0.06f),
            topLeft = topLeft + Offset(jarW * 0.10f, jarH * 0.08f),
            size = androidx.compose.ui.geometry.Size(jarW * 0.10f, jarH * 0.80f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(60f, 60f)
        )
    }
}
