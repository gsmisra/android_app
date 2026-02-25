package com.dev.gauravmisra.happyfingers.tools


import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dev.gauravmisra.happyfingers.data.ChoiceItem
import java.util.Locale

@Composable
fun ChoiceBoardTTS() {
    val ctx = LocalContext.current

    // Holds the engine instance for speaking
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    // Optional: track readiness so you can disable buttons until initialized
    var ttsReady by remember { mutableStateOf(false) }

    /**
     * IMPORTANT: do NOT reference "engine" inside its own constructor callback.
     * Use a local variable (localTts) that exists before the callback runs.
     */
    DisposableEffect(Unit) {
        var localTts: TextToSpeech? = null

        localTts = TextToSpeech(ctx) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = localTts?.setLanguage(Locale.US)
                ttsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED
            } else {
                ttsReady = false
            }
        }

        tts = localTts

        onDispose {
            localTts?.stop()
            localTts?.shutdown()
            tts = null
        }
    }

    val items = remember {
        mutableStateListOf(
            ChoiceItem("want", "I want", "I want"),
            ChoiceItem("help", "Help", "Help please"),
            ChoiceItem("break", "Break", "I need a break"),
            ChoiceItem("drink", "Drink", "I want a drink"),
            ChoiceItem("play", "Play", "I want to play"),
            ChoiceItem("more", "More", "More please"),
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Tap to speak", style = MaterialTheme.typography.titleLarge)

        if (!ttsReady) {
            Text(
                "Speech is getting ready… (or not available on this device)",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // simple grid: 2 columns
        val rows = items.chunked(2)
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { item ->
                    ElevatedCard(
                        modifier = Modifier.weight(1f).height(90.dp),
                        onClick = {
                            // Use a unique utteranceId to avoid collisions
                            val utteranceId = "${item.id}_${System.nanoTime()}"
                            tts?.speak(
                                item.phrase,
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                utteranceId
                            )
                        },
                        enabled = tts != null // or: enabled = ttsReady
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(12.dp)
                        ) {
                            Text(item.label, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(10.dp))
        Text(
            "Caregiver tip: model the button press + phrase during real routines.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
