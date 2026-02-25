package com.dev.gauravmisra.lostinmystory.data



object StoryValidator {

    data class ValidationResult(
        val errors: List<String>,
        val warnings: List<String>
    ) {
        val isValid: Boolean get() = errors.isEmpty()

        fun asPrettyString(): String {
            val sb = StringBuilder()
            if (errors.isNotEmpty()) {
                sb.appendLine("❌ ERRORS (${errors.size}):")
                errors.forEach { sb.appendLine(" - $it") }
            }
            if (warnings.isNotEmpty()) {
                sb.appendLine("⚠️ WARNINGS (${warnings.size}):")
                warnings.forEach { sb.appendLine(" - $it") }
            }
            return sb.toString().trimEnd()
        }
    }

    private val allowedCategories = setOf("mystery", "mind", "scifi")

    fun validate(payload: StoriesPayloadDTO): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // --- story id uniqueness
        val storyIds = payload.stories.map { it.id.trim() }
        val dupStoryIds = storyIds.groupBy { it }
            .filter { it.key.isNotBlank() && it.value.size > 1 }
            .keys
        dupStoryIds.forEach { errors.add("Duplicate story id: '$it'") }

        payload.stories.forEach { story ->
            val sid = story.id.trim()
            val title = story.title.trim()
            val cat = story.category.trim().lowercase()
            val start = story.startNodeId.trim()

            if (sid.isBlank()) errors.add("Story has blank id (title='$title')")
            if (title.isBlank()) errors.add("Story '$sid' has blank title")
            if (start.isBlank()) errors.add("Story '$sid' has blank startNodeId")
            if (cat !in allowedCategories) {
                errors.add("Story '$sid' has invalid category '$cat' (allowed: mystery, mind, scifi)")
            }

            // --- nodes checks
            val nodeIds = story.nodes.map { it.id.trim() }
            val dupNodes = nodeIds.groupBy { it }
                .filter { it.key.isNotBlank() && it.value.size > 1 }
                .keys
            dupNodes.forEach { errors.add("Story '$sid' has duplicate node id: '$it'") }

            val nodeMap = story.nodes.associateBy { it.id.trim() }

            if (start.isNotBlank() && start !in nodeMap) {
                errors.add("Story '$sid' startNodeId '$start' not found in nodes")
            }

            // --- choices checks
            story.nodes.forEach { node ->
                val nid = node.id.trim()
                if (nid.isBlank()) {
                    errors.add("Story '$sid' has a node with blank id")
                    return@forEach
                }

                // ending node should normally not have choices (warning only)
                if (!node.endingTitle.isNullOrBlank() && node.choices.isNotEmpty()) {
                    warnings.add("Story '$sid' node '$nid' has endingTitle but also has choices (usually a mistake)")
                }

                // choice ids unique within node
                val choiceIds = node.choices.map { it.id.trim() }.filter { it.isNotBlank() }
                val dupChoiceIds = choiceIds.groupBy { it }
                    .filter { it.value.size > 1 }
                    .keys
                dupChoiceIds.forEach { errors.add("Story '$sid' node '$nid' has duplicate choice id: '$it'") }

                node.choices.forEach { ch ->
                    val to = ch.toNodeId.trim()
                    if (to.isBlank()) {
                        errors.add("Story '$sid' node '$nid' choice '${ch.id}' has blank toNodeId")
                    } else if (to !in nodeMap) {
                        errors.add("Story '$sid' node '$nid' choice '${ch.id}' points to missing node '$to'")
                    }
                }
            }

            // --- reachability (warnings)
            if (start.isNotBlank() && start in nodeMap) {
                val reachable = computeReachableNodes(story, start)
                val unreachable = nodeMap.keys - reachable
                if (unreachable.isNotEmpty()) {
                    warnings.add("Story '$sid' has unreachable nodes: ${unreachable.sorted().joinToString(", ")}")
                }
            }

            // --- ending existence (warnings)
            val endingsCount = story.nodes.count { !it.endingTitle.isNullOrBlank() }
            if (endingsCount == 0) warnings.add("Story '$sid' has no endings (endingTitle is missing everywhere)")

            val choicesCount = story.nodes.sumOf { it.choices.size }
            if (choicesCount == 0) warnings.add("Story '$sid' has no choices (story is static text)")
        }

        return ValidationResult(errors, warnings)
    }

    private fun computeReachableNodes(story: StoryDTO, startNodeId: String): Set<String> {
        val nodeMap = story.nodes.associateBy { it.id.trim() }
        val visited = mutableSetOf<String>()
        val stack = ArrayDeque<String>()
        stack.add(startNodeId)

        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (!visited.add(current)) continue

            val node = nodeMap[current] ?: continue
            node.choices.forEach { choice ->
                val next = choice.toNodeId.trim()
                if (next.isNotBlank() && next !in visited) {
                    stack.add(next)
                }
            }
        }
        return visited
    }
}
