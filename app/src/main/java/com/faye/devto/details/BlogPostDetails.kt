package com.faye.devto.details

import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.faye.devto.model.BlogPost
import java.util.*


@Composable
fun BlogPostDetailsScreen(viewModel: BlogPostDetailsViewModel) {
	val state by viewModel.state.collectAsState(initial = BlogPostDetailsViewModel.INITIAL_STATE)

	BlogPostDetailsLayout(blogPost = state.blogPost)
}

@Composable
private fun BlogPostDetailsLayout(blogPost: BlogPost?) {
	if (blogPost == null) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			CircularProgressIndicator()
			Text(text = "loading", style = MaterialTheme.typography.bodyLarge)
		}
	} else {
		Column(
			modifier = Modifier.padding(16.dp)
		) {
			Text(text = blogPost.title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.semantics {
				testTag = "blogPost name"
			})
			if (blogPost.description.isNotEmpty()) {
				Spacer(modifier = Modifier.size(2.dp))
				Text(text = blogPost.description, style = MaterialTheme.typography.titleSmall)
			}

			Spacer(modifier = Modifier.size(8.dp))
			Divider()
			Spacer(modifier = Modifier.size(8.dp))
			AnimatedVisibility(visible = blogPost.body != null) {
				AndroidView(
					factory = { context ->
						val scroll = ScrollView(context)
						scroll.addView(TextView(context))
						scroll
					},
					update = {
						val textView = it.getChildAt(0) as? TextView
						textView?.text = HtmlCompat.fromHtml(blogPost.body ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
					}
				)
			}
		}
	}
}

@Preview
@Composable
fun BlogPostDetailsLayoutWithoutPost() {
	BlogPostDetailsLayout(blogPost = null)
}

@Preview
@Composable
fun BlogPostDetailsLayoutWithoutText() {
	val blogPost = BlogPost(
		id = 1,
		title = "Title",
		description = "A small description",
		body = null,
		url = "",
		coverImageUrl = "",
		path = "",
		publishTimestamp = Date()
	)
	BlogPostDetailsLayout(blogPost = blogPost)
}

@Preview
@Composable
fun BlogPostDetailsLayoutWithText() {
	val blogPost = BlogPost(
		id = 1,
		title = "Title",
		description = "A small description",
		body = "Here ist the article text this can be l${"o".repeat(100)}ng",
		url = "",
		coverImageUrl = "",
		path = "",
		publishTimestamp = Date()
	)
	BlogPostDetailsLayout(blogPost = blogPost)
}