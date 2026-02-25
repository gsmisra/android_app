package com.dev.gauravmisra.the_offencive_joker


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * ViewModel for:
 * - Loading jokes from assets/data.json
 * - Building dynamic categories
 * - Filtering deck by category
 * - Tracking current card and "answer revealed" state
 */
class JokerViewModel : ViewModel() {

    // ---------- Public UI State ----------
    private val _uiState = MutableStateFlow(JokerUiState())
    val uiState: StateFlow<JokerUiState> = _uiState.asStateFlow()

    // JSON parser
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Load the JSON once on app start (call from MainActivity/Composable using LaunchedEffect).
     */
    fun loadFromAssets(context: Context, assetFileName: String = "data.json") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val raw = context.assets.open(assetFileName)
                    .bufferedReader()
                    .use { it.readText() }

                val items = json.decodeFromString<List<JokeItem>>(raw)

                val categories = items
                    .map { it.category.trim() }
                    .filter { it.isNotBlank() }
                    .distinct()
                    .sorted()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    allItems = items,
                    categories = categories,
                    selectedCategory = null,
                    deck = emptyList(),
                    currentIndex = 0,
                    isAnswerRevealed = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load $assetFileName: ${e.message}"
                )
            }
        }
    }

    /**
     * User selects a category from the landing page.
     * This builds a deck for that category.
     */
    fun selectCategory(category: String) {
        val all = _uiState.value.allItems
        val filtered = all.filter { it.category.equals(category, ignoreCase = true) }

        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            deck = filtered,
            currentIndex = 0,
            isAnswerRevealed = false,
            errorMessage = if (filtered.isEmpty()) "No items found for category: $category" else null
        )
    }

    /**
     * Go back to landing page category list.
     */
    fun clearCategory() {
        _uiState.value = _uiState.value.copy(
            selectedCategory = null,
            deck = emptyList(),
            currentIndex = 0,
            isAnswerRevealed = false,
            errorMessage = null
        )
    }

    /**
     * Current card item (question/answer).
     */
    fun currentItem(): JokeItem? {
        val deck = _uiState.value.deck
        val idx = _uiState.value.currentIndex
        return if (deck.isNotEmpty() && idx in deck.indices) deck[idx] else null
    }

    /**
     * Called when user swipes UP to reveal the answer.
     * Your UI can observe isAnswerRevealed to switch question->answer.
     *
     * MP3 playback should be triggered by the UI when this flips to true
     * (or by calling a callback after this function).
     */
    fun revealAnswer() {
        _uiState.value = _uiState.value.copy(isAnswerRevealed = true)
    }

    /**
     * Move to the next card after left/right swipe.
     * (You can decide whether both left and right do same behavior or different.)
     */
    fun nextCard() {
        val deck = _uiState.value.deck
        if (deck.isEmpty()) return

        val nextIndex = (_uiState.value.currentIndex + 1) % deck.size

        _uiState.value = _uiState.value.copy(
            currentIndex = nextIndex,
            isAnswerRevealed = false
        )
    }

    /**
     * Move to previous card if you want support.
     */
    fun previousCard() {
        val deck = _uiState.value.deck
        if (deck.isEmpty()) return

        val prevIndex =
            if (_uiState.value.currentIndex - 1 < 0) deck.size - 1
            else _uiState.value.currentIndex - 1

        _uiState.value = _uiState.value.copy(
            currentIndex = prevIndex,
            isAnswerRevealed = false
        )
    }

    fun backToCategories() {
        _uiState.update { it.copy(selectedCategory = null, isAnswerRevealed = false) }
    }
}

/**
 * Compose-friendly UI state.
 */
data class JokerUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val allItems: List<JokeItem> = emptyList(),
    val categories: List<String> = emptyList(),

    val selectedCategory: String? = null,
    val deck: List<JokeItem> = emptyList(),
    val currentIndex: Int = 0,
    val isAnswerRevealed: Boolean = false
)

/**
 * This matches your data.json schema:
 * { "category": "...", "question": "...", "answer": "..." }
 */
@Serializable
data class JokeItem(
    @SerialName("category") val category: String,
    @SerialName("question") val question: String,
    @SerialName("answer") val answer: String
)
