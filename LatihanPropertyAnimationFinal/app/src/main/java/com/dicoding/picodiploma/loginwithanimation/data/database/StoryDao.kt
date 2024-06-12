package com.dicoding.picodiploma.loginwithanimation.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.picodiploma.loginwithanimation.data.api.ListStoryItem

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, ListStoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<ListStoryItem>)

    @Query("DELETE FROM story")
    suspend fun clearAll()
}