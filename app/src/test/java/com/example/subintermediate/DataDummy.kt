package com.example.subintermediate

import com.example.subintermediate.data.api.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                photoUrl = "photoUrl + $i",
                createdAt = "createdAt $i",
                name = "name $i",
                description = "description $i",
                lon = i,
                id = i.toString(),
                lat = i
            )
            items.add(story)
        }
        return items
    }
}
