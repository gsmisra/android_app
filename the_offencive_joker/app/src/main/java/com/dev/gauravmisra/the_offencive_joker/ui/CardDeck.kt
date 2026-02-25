package com.dev.gauravmisra.the_offencive_joker.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun CardDeck(
    question: String,
    answer: String,
    showAnswer: Boolean,
    modifier: Modifier = Modifier,
    stackSize: Int = 3,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeUp: () -> Unit
) {
    val screenWidthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var swipeUpTriggered by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(question, answer) {
        swipeUpTriggered = false
        offsetX.snapTo(0f)
        offsetY.snapTo(0f)
    }

    val swipeThresholdPx = with(LocalDensity.current) { 120.dp.toPx() }
    val swipeUpThresholdPx = with(LocalDensity.current) { 110.dp.toPx() }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val actualStack = stackSize.coerceAtLeast(1)
        for (i in actualStack - 1 downTo 1) {
            val scale = 1f - (i * 0.04f)
            val yShift = (i * 14).dp

            DeckCardShell(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(320.dp)
                    .offset(y = yShift)
                    .graphicsScale(scale)
                    .alphaClamp(0.92f - i * 0.08f),
                title = "The Offensive Joker",
                body = "Swipe a card…",
                isAnswer = false,
                accent = if (i % 2 == 0) Color(0xFFFFEB3B) else Color(0xFF00E5FF)
            )
        }

        val topAccent = Color(0xFFFF4D6D)
        DeckCardShell(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .height(340.dp)
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .pointerInput(showAnswer) {
                    detectDragGestures(
                        onDragStart = { },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val newX = offsetX.value + dragAmount.x
                            val newY = offsetY.value + dragAmount.y
                            scope.launch {
                                offsetX.snapTo(newX)
                                offsetY.snapTo(newY)
                            }
                        },
                        onDragEnd = {
                            val x = offsetX.value
                            val y = offsetY.value
                            val absX = abs(x)
                            val absY = abs(y)

                            when {
                                (y < -swipeUpThresholdPx && absY > absX) -> {
                                    if (!swipeUpTriggered) {
                                        swipeUpTriggered = true
                                        onSwipeUp()
                                    }
                                    scope.launch {
                                        offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessMediumLow))
                                        offsetY.animateTo(0f, spring(stiffness = Spring.StiffnessMediumLow))
                                    }
                                }
                                (x > swipeThresholdPx && absX >= absY) -> {
                                    scope.launch {
                                        offsetX.animateTo(screenWidthPx * 1.2f, spring(stiffness = Spring.StiffnessLow))
                                        offsetY.animateTo(0f, spring(stiffness = Spring.StiffnessLow))
                                        onSwipeRight()
                                        offsetX.snapTo(0f)
                                        offsetY.snapTo(0f)
                                        swipeUpTriggered = false
                                    }
                                }
                                (x < -swipeThresholdPx && absX >= absY) -> {
                                    scope.launch {
                                        offsetX.animateTo(-screenWidthPx * 1.2f, spring(stiffness = Spring.StiffnessLow))
                                        offsetY.animateTo(0f, spring(stiffness = Spring.StiffnessLow))
                                        onSwipeLeft()
                                        offsetX.snapTo(0f)
                                        offsetY.snapTo(0f)
                                        swipeUpTriggered = false
                                    }
                                }
                                else -> {
                                    scope.launch {
                                        offsetX.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                                        offsetY.animateTo(0f, spring(stiffness = Spring.StiffnessMedium))
                                    }
                                }
                            }
                        }
                    )
                },
            title = "The Offensive Joker",
            body = if (showAnswer) answer else question,
            isAnswer = showAnswer,
            accent = topAccent
        )
    }
}

@Composable
private fun DeckCardShell(
    modifier: Modifier,
    title: String,
    body: String,
    isAnswer: Boolean,
    accent: Color
) {
    val shape = RoundedCornerShape(28.dp)
    val borderBrush = Brush.linearGradient(
        colors = listOf(accent, Color(0xFF7C4DFF), Color(0xFF00E5FF))
    )

    Card(
        modifier = modifier.shadow(18.dp, shape, clip = false),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        0f to Color(0xFF232326),
                        1f to Color(0xFF151517)
                    )
                )
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(2.dp)
                    .background(borderBrush, RoundedCornerShape(26.dp))
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(6.dp)
                    .background(Color(0xFF1C1C1E), RoundedCornerShape(22.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFEB3B)
                    )
                    Text(
                        text = if (isAnswer) "ANSWER" else "QUESTION",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFBDBDBD)
                    )
                }

                Text(
                    text = body,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Text(
                    text = "← swipe  •  swipe →  •  swipe ↑ to reveal",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF9E9E9E)
                )
            }
        }
    }
}

private fun Modifier.graphicsScale(scale: Float): Modifier =
    this.then(Modifier.graphicsLayer(scaleX = scale, scaleY = scale))

private fun Modifier.alphaClamp(alpha: Float): Modifier =
    this.then(Modifier.graphicsLayer(alpha = alpha.coerceIn(0f, 1f)))