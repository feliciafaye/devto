package com.faye.devto.overview

import androidx.paging.PagingData
import com.faye.devto.model.BlogPost
import kotlinx.coroutines.flow.Flow

data class BlogPostsOverviewViewState(
	val blogPosts: Flow<PagingData<BlogPost>>
)