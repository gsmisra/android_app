package com.dev.gauravmisra.lostinmystory.ui


import android.app.Application
import androidx.lifecycle.AndroidViewModel

import com.dev.gauravmisra.lostinmystory.data.StoryRepository
import com.dev.gauravmisra.lostinmystory.data.StoryRepositoryImpl
import com.dev.gauravmisra.lostinmystory.model.Category
import com.dev.gauravmisra.lostinmystory.model.Node
import com.dev.gauravmisra.lostinmystory.model.Story


class StoryViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: StoryRepository = StoryRepositoryImpl(app.applicationContext)

    fun categories(): List<Category> = repo.categories()
    fun stories(category: Category): List<Story> = repo.storiesByCategory(category)
    fun story(id: String): Story? = repo.storyById(id)

    fun node(storyId: String, nodeId: String): Node? =
        repo.storyById(storyId)?.nodes?.get(nodeId)
}
