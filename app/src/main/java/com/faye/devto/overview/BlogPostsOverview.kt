package com.faye.devto.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import com.faye.devto.R
import com.faye.devto.model.BlogPost

@Composable
fun BlogPostsOverviewScreen(
	vm: BlogPostsOverviewViewModel,
	onBlogPostClicked: (BlogPost) -> Unit
) {

	val state by vm.state.collectAsState(initial = BlogPostsOverviewViewModel.INITIAL_STATE)
	val blogPosts = state.blogPosts.collectAsLazyPagingItems()

	BlogPostsOverviewLayout(blogPosts = blogPosts) {
		onBlogPostClicked(it)
	}

}

@Composable
fun BlogPostsOverviewLayout(blogPosts: LazyPagingItems<BlogPost>, onBlogPostClicked: (BlogPost) -> Unit) {
	Box(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxSize()
	) {
		LazyColumn(
			modifier = Modifier.fillMaxSize()
		) {
			itemsIndexed(
				items = blogPosts,
				key = { _, blogPost -> blogPost.id }
			) { idx, blogPost ->
				if (blogPost != null) {
					BlogPostItem(
						blogPost = blogPost,
						modifier = Modifier
							.fillMaxWidth()
							.clickable { onBlogPostClicked(blogPost) }
							.padding(top = 2.dp, bottom = 2.dp)
					)
				}
				if(idx < blogPosts.itemCount - 1){
					Divider()
				}
			}
		}
		AnimatedVisibility(
			visible = blogPosts.loadState.append is LoadState.Loading,
			modifier = Modifier.align(Alignment.BottomCenter)
		) {
			CircularProgressIndicator()
		}
		AnimatedVisibility(
			visible = blogPosts.loadState.append is LoadState.Error,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(16.dp)
		) {
			FloatingActionButton(onClick = { blogPosts.retry() }) {
				Icon(
					imageVector = Icons.Default.Warning,
					contentDescription = stringResource(id = R.string.retry_loading)
				)
			}
		}
	}
}

@Composable
fun BlogPostItem(blogPost: BlogPost, modifier: Modifier = Modifier) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier.semantics {
			testTag = "blog post ${blogPost.id}"
		}
	) {
		if (blogPost.coverImageUrl != null) {
			AsyncImage(
				model = blogPost.coverImageUrl,
				contentDescription = "",
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.padding(start = 8.dp)
					.clip(CircleShape)
					.size(50.dp)
			)
		}
		Column(Modifier.padding(8.dp)) {
			Text(text = blogPost.title, style = MaterialTheme.typography.bodyLarge)
			if (blogPost.description.isNotEmpty()) {
				Text(text = blogPost.description, style = MaterialTheme.typography.bodySmall)
			}
		}
	}
}