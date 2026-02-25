package com.dev.gauravmisra.lostinmystory.data


import android.content.Context
import android.util.Log
import kotlinx.serialization.json.Json


object StoryAssetLoader {

    private const val TAG = "StoryAssetLoader"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun loadFromAssets(context: Context, fileName: String = "stories.json"): StoriesPayloadDTO {
        val text = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val payload = json.decodeFromString(StoriesPayloadDTO.serializer(), text)

        // ✅ Validate payload
        val result = StoryValidator.validate(payload)

        // Log warnings
        if (result.warnings.isNotEmpty()) {
            Log.w(TAG, result.asPrettyString())
        }

        // Fail fast on errors (best while you build content)
        if (!result.isValid) {
            Log.e(TAG, result.asPrettyString())
            throw IllegalStateException("stories.json validation failed:\n${result.asPrettyString()}")
        }

        return payload
    }
}
