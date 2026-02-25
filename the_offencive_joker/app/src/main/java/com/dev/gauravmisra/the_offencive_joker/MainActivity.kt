package com.dev.gauravmisra.the_offencive_joker


import android.os.Bundle
import android.media.SoundPool
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.gauravmisra.the_offencive_joker.ui.CategoryScreen
import com.dev.gauravmisra.the_offencive_joker.ui.CardDeck
import com.dev.gauravmisra.the_offencive_joker.ui.theme.JokerTheme
import androidx.compose.ui.platform.LocalContext
import com.dev.gauravmisra.the_offencive_joker.audio.playSwipeSound


class MainActivity : ComponentActivity() {
    private lateinit var soundPool: SoundPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundPool = SoundPool.Builder().setMaxStreams(1).build()
        enableEdgeToEdge()


        setContent {
            JokerTheme {
                val vm: JokerViewModel = viewModel()
                val uiState by vm.uiState.collectAsState()
                val context = LocalContext.current   // ✅ composable context

                LaunchedEffect(Unit) {
                    vm.loadFromAssets(this@MainActivity)
                }

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    uiState.errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "Error loading data",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = uiState.errorMessage ?: "Unknown error",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    uiState.selectedCategory == null && uiState.categories.isNotEmpty() -> {
                        CategoryScreen(uiState.categories) { vm.selectCategory(it) }
                    }
                    uiState.selectedCategory != null && uiState.deck.isNotEmpty() -> {
                        val currentItem = vm.currentItem()
                        if (currentItem != null) {
                            CardScreen(
                                vm = vm,
                                uiState = uiState,
                                currentItem = currentItem,
                                context = context
                            )
                        }
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text("No data available")
                        }
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CardScreen(
        vm: JokerViewModel,
        uiState: JokerUiState,
        currentItem: JokeItem,
        context: android.content.Context
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(uiState.selectedCategory ?: "") },
                    navigationIcon = {
                        IconButton(onClick = { vm.backToCategories() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                CardDeck(
                    question = currentItem.question,
                    answer = currentItem.answer,
                    showAnswer = uiState.isAnswerRevealed,
                    stackSize = 3,
                    onSwipeUp = {
                        vm.revealAnswer()
                        playSwipeSound(context)
                    },
                    onSwipeLeft = { vm.nextCard() },
                    onSwipeRight = { vm.nextCard() }
                )
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}
