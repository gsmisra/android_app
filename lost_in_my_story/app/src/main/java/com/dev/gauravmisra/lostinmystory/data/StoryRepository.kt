package com.dev.gauravmisra.lostinmystory.data

import com.dev.gauravmisra.lostinmystory.model.Category
import com.dev.gauravmisra.lostinmystory.model.Story


interface StoryRepository {
    fun categories(): List<Category>
    fun storiesByCategory(category: Category): List<Story>
    fun storyById(id: String): Story?
}
