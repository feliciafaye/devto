package com.faye.devto.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.faye.devto.repository.BlogPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class BlogPostsOverviewViewModel @Inject constructor(
    blogPostRepository: BlogPostRepository
) : ViewModel() {

    private val _state = MutableStateFlow(INITIAL_STATE)
    val state: Flow<BlogPostsOverviewViewState> = _state

    init {
        _state.value = BlogPostsOverviewViewState(
            blogPosts = blogPostRepository.blogPosts()
                .cachedIn(viewModelScope) // keep position in list when navigating
        )
    }

    companion object {
        val INITIAL_STATE = BlogPostsOverviewViewState(emptyFlow())
    }
}