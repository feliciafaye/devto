package com.faye.devto.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
	entities = [BlogPostEntity::class],
	version = 1,
	exportSchema = false
)
@TypeConverters(
	DateConverter::class
)
abstract class DevToDatabase : RoomDatabase() {
	abstract fun getBlogPostDao(): BlogPostDao

}