package com.dev.gauravmisra.happyfingers.games.play



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.hypot

@Composable
fun TracingPath() {
    // Simple “S” path as points
    val pathPoints = remember {
        List(60) { i ->
            val t = i / 59f
            val x = 150f + t * 800f
            val y = 300f + (kotlin.math.sin(t * Math.PI * 2).toFloat() * 160f)
            Offset(x, y)
        }
    }

    var progress by remember { mutableStateOf(0) }
    var isDone by remember { mutableStateOf(false) }

    Canvas(
        Modifier.fillMaxSize().pointerInput(Unit) {
            detectDragGestures { _, drag ->
                // ignore raw drag; we use pointer positions via event below (simplified)
            }
        }.pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, _ ->
                    val p = change.position
                    if (isDone) return@detectDragGestures

                    val target = pathPoints[progress]
                    val dist = hypot(p.x - target.x, p.y - target.y)

                    // forgiving tolerance (increase for age 3–5)
                    val tol = 55f
                    if (dist <= tol) {
                        progress = (progress + 1).coerceAtMost(pathPoints.lastIndex)
                        if (progress >= pathPoints.lastIndex) isDone = true
                    }
                }
            )
        }
    ) {
        drawRect(Color(0xFFFFFBF2))

        // Draw guide path
        for (i in 0 until pathPoints.lastIndex) {
            drawLine(
                color = Color(0xFFB0BEC5),
                start = pathPoints[i],
                end = pathPoints[i + 1],
                strokeWidth = 18f
            )
        }

        // Draw progress path
        for (i in 0 until progress.coerceAtLeast(1)) {
            drawLine(
                color = Color(0xFF4F7CAC),
                start = pathPoints[i],
                end = pathPoints[i + 1],
                strokeWidth = 20f
            )
        }

        // Target dot
        val target = pathPoints[progress]
        drawCircle(Color(0xFF7FB069), 20f, target)

        if (isDone) {
            drawCircle(Color(0xFF7FB069).copy(alpha = 0.25f), 140f, pathPoints.last())
        }
    }
}
