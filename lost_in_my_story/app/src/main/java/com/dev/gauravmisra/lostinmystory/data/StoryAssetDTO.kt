package com.dev.gauravmisra.lostinmystory.data

import kotlinx.serialization.Serializable

@Serializable
data class StoriesPayloadDTO(
    val stories: List<StoryDTO>
)

@Serializable
data class StoryDTO(
    val id: String,
    val title: String,
    val category: String,
    val tags: List<String> = emptyList(),
    val startNodeId: String,
    val nodes: List<NodeDTO>
)

@Serializable
data class NodeDTO(
    val id: String,
    val text: String,
    val choices: List<ChoiceDTO> = emptyList(),
    val endingTitle: String? = null,
    val backgroundImage: String? = null
)

@Serializable
data class ChoiceDTO(
    val id: String,
    val text: String,
    val toNodeId: String
)
