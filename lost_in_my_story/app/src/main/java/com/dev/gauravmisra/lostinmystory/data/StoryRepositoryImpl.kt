package com.dev.gauravmisra.lostinmystory.data

import com.dev.gauravmisra.lostinmystory.model.Category
import com.dev.gauravmisra.lostinmystory.model.Story
import android.content.Context
import com.dev.gauravmisra.lostinmystory.model.Choice
import com.dev.gauravmisra.lostinmystory.model.Node


class StoryRepositoryImpl(
    private val context: Context
) : StoryRepository {

    // cached in memory after first load
    private val stories: List<Story> by lazy {
        StoryAssetLoader.loadFromAssets(context).stories.map { it.toDomain() }
    }

    override fun categories(): List<Category> = Category.entries.toList()

    override fun storiesByCategory(category: Category): List<Story> =
        stories.filter { it.category == category }

    override fun storyById(id: String): Story? =
        stories.firstOrNull { it.id == id }

    private fun StoryDTO.toDomain(): Story {
        val mappedCategory = when (category.lowercase()) {
            "mystery" -> Category.MYSTERY
            "mind" -> Category.MIND_GAMES
            "scifi" -> Category.DARK_SCIFI
            else -> Category.MYSTERY
        }

        val nodeMap = nodes.associate { node ->
            node.id to Node(
                id = node.id,
                text = node.text,
                choices = node.choices.map { Choice(it.id, it.text, it.toNodeId) },
                endingTitle = node.endingTitle,
                backgroundImage = node.backgroundImage
            )
        }

        return Story(
            id = id,
            title = title,
            category = mappedCategory,
            tags = tags,
            startNodeId = startNodeId,
            nodes = nodeMap
        )
    }
}
