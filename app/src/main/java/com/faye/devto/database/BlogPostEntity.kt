package com.faye.devto.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "BlogPost")
data class BlogPostEntity(
	@PrimaryKey val id: Int = 0,
	val title: String,
	val description: String,
	val path: String,
	val body: String?,
	val url: String,
	val coverImageUrl: String?,
	val publishTimestamp: Date?
)