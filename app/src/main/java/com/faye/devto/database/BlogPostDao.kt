package com.faye.devto.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BlogPostDao {
	@Query("SELECT * FROM BlogPost ORDER BY publishTimestamp DESC")
	fun getBlogPostsPaged(): PagingSource<Int, BlogPostEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun storeBlogPosts(entities: List<BlogPostEntity>)

	@Query("SELECT * FROM BlogPost WHERE id = :id")
	fun getBlogPostById(id: Int): Flow<BlogPostEntity>

	@Update
	suspend fun updatePost(blogPostEntity: BlogPostEntity)
}