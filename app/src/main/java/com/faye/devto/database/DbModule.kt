package com.faye.devto.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    fun provideBlogPostDatabase(@ApplicationContext context: Context): DevToDatabase =
        Room.databaseBuilder(context, DevToDatabase::class.java, "blogpost.db")
            .build()

    @Provides
    fun provideBlogPostDao(db: DevToDatabase): BlogPostDao =
        db.getBlogPostDao()

}