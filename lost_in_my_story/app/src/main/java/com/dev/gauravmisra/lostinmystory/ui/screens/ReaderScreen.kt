package com.dev.gauravmisra.lostinmystory.ui.screens


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.gauravmisra.lostinmystory.ui.StoryViewModel
import com.dev.gauravmisra.lostinmystory.ui.rememberAssetImageBitmap
import kotlin.math.max
import kotlin.math.min


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    storyId: String,
    vm: StoryViewModel,
    onBack: () -> Unit
) {
    val story = vm.story(storyId)

    // current node
    var nodeId by remember { mutableStateOf(story?.startNodeId ?: "") }
    val node = remember(nodeId) { vm.node(storyId, nodeId) }

    // track visited nodes for progress
    val visited = remember { mutableStateListOf<String>() }
    LaunchedEffect(nodeId) {
        if (nodeId.isNotBlank()) visited.add(nodeId)
    }

    // font scale control
    var fontScale by remember { mutableFloatStateOf(1.0f) }

    val progress = remember(story, visited.size) {
        computeStoryProgress(storyId = storyId, vm = vm, visited = visited)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = story?.title ?: "Story",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Progress: ${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.titleLarge)
                    }
                },
                actions = {
                    TextButton(onClick = { fontScale = max(0.85f, fontScale - 0.1f) }) {
                        Text("A−")
                    }
                    TextButton(onClick = { fontScale = min(1.6f, fontScale + 0.1f) }) {
                        Text("A+")
                    }
                }
            )
        }
    ) { padding ->

        val bgPath = node?.backgroundImage

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            /* =========================
               🎨 BACKGROUND IMAGE LAYER
               ========================= */
            Crossfade(
                targetState = bgPath ?: "",
                label = "SceneBackground"
            ) { path ->
                val image = rememberAssetImageBitmap(
                    path.takeIf { it.isNotBlank() }
                )

                if (image != null) {
                    Image(
                        bitmap = image,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
            }

            /* =========================
               🌑 DARK SCRIM OVERLAY
               ========================= */
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.55f))
            )

            /* =========================
               📖 FOREGROUND CONTENT
               ========================= */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Font size",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Slider(
                            value = fontScale,
                            onValueChange = { fontScale = it },
                            valueRange = 0.85f..1.6f
                        )
                    }
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val base = MaterialTheme.typography.bodyLarge
                        Text(
                            text = node?.text ?: "Missing node",
                            style = base.copy(
                                fontSize = (base.fontSize.value * fontScale).sp,
                                lineHeight = (22f * fontScale).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                /* =========================
                   🏁 ENDING OR CHOICES
                   ========================= */
                node?.endingTitle?.let { ending ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = ending,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.SemiBold
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(onClick = {
                                    visited.clear()
                                    nodeId = story?.startNodeId ?: nodeId
                                }) {
                                    Text("Restart")
                                }

                                OutlinedButton(onClick = onBack) {
                                    Text("Back")
                                }
                            }
                        }
                    }
                } ?: run {
                    Text(
                        text = "Choose:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    node?.choices?.forEach { choice ->
                        Button(
                            onClick = { nodeId = choice.toNodeId },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(choice.text)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {
                            visited.clear()
                            nodeId = story?.startNodeId ?: nodeId
                        }) {
                            Text("Restart story")
                        }

                        FilledTonalButton(onClick = { }) {
                            Text("Info")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Approximate story progress:
 * distinct visited nodes / total playable nodes
 */
private fun computeStoryProgress(
    storyId: String,
    vm: StoryViewModel,
    visited: List<String>
): Float {
    val story = vm.story(storyId) ?: return 0f
    val playable = story.nodes.values.count { it.endingTitle == null }.coerceAtLeast(1)
    val visitedPlayable = visited.distinct().count { id ->
        story.nodes[id]?.endingTitle == null
    }
    return (visitedPlayable.toFloat() / playable.toFloat()).coerceIn(0f, 1f)
}