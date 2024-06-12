package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.api.StoryResponse

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "created_at",
                "name $i",
                "description $i",
                1.0,
                "user-Q3Yg6uV7j4X6LXxT",
                2.0,
            )
            items.add(story)
        }
        return items
    }
}