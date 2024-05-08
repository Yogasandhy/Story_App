package com.example.subintermediate.ui.story

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.subintermediate.data.api.ApiService
import com.example.subintermediate.data.api.ListStoryItem

class StoryPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val nextPage = params.key ?: 1
            val response = apiService.getStories(nextPage)

            LoadResult.Page(
                data = response.listStory,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.listStory.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition
    }
}

