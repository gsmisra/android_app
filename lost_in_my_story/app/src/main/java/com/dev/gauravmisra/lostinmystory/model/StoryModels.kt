package com.dev.gauravmisra.lostinmystory.model


enum class Category(val id: String, val title: String) {
    MYSTERY("mystery", "Mystery / Thriller"),
    MIND_GAMES("mind", "Mind Games"),
    DARK_SCIFI("scifi", "Dark Sci‑Fi")
}

data class Story(
    val id: String,
    val title: String,
    val category: Category,
    val tags: List<String>,
    val startNodeId: String,
    val nodes: Map<String, Node>
)

data class Node(
    val id: String,
    val text: String,
    val choices: List<Choice> = emptyList(),
    val endingTitle: String? = null,
    val backgroundImage: String? = null
)

data class Choice(
    val id: String,
    val text: String,
    val toNodeId: String
)


class StoryModels {
}