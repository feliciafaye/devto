package com.faye.devto.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.faye.devto.repository.BlogPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BlogPostDetailsViewModel @Inject constructor(
	blogPostRepository: BlogPostRepository,
	savedStateHandle: SavedStateHandle
) : ViewModel() {
	@OptIn(ExperimentalCoroutinesApi::class)
	val state: Flow<BlogPostDetailsViewState> = savedStateHandle
		.getLiveData<String>(paramId)
		.asFlow()
		.map {
			it.toIntOrNull()
		}
		.filterNotNull()
		.flatMapLatest {
			blogPostRepository.getBlogPostsFlow(it)
		}.map { blogPost ->
			BlogPostDetailsViewState(
			blogPost = blogPost.copy(
				id = blogPost.id,
				title = blogPost.title,
				description = blogPost.description
			)
		) }

	companion object {
		val INITIAL_STATE = BlogPostDetailsViewState()
	}
}