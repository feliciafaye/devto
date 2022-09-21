package com.faye.devto

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faye.devto.details.paramId

class NavGraph(private val navHostController: NavHostController) {

	private val startDestination: String = Destination.BlogPostsOverview.routePattern

	@Composable
	fun NavHost(content: NavGraphBuilder.() -> Unit) {
		NavHost(navController = navHostController, startDestination = startDestination) {
			content()
		}
	}

	fun NavGraphBuilder.addBlogPostsOverviewContent(content: @Composable BlogPostsOverviewContext.(NavBackStackEntry) -> Unit) {
		composable(Destination.BlogPostsOverview.routePattern) {
			BlogPostsOverviewContext(navHostController).content(it)
		}
	}

	fun NavGraphBuilder.addBlogPostDetailsContent(content: @Composable BlogPostDetailContext.(NavBackStackEntry) -> Unit) {
		composable(Destination.BlogPostDetails.routePattern) {
			BlogPostDetailContext().content(it)
		}
	}
}

class BlogPostsOverviewContext(private val navHostController: NavHostController) {
	fun navigateToDetails(id: Int) {
		navHostController.navigate(Destination.BlogPostDetails.createRoute(id))
	}
}

class BlogPostDetailContext()

private sealed class Destination(
	val name: String
) {
	abstract val routePattern: String

	object BlogPostsOverview : Destination("overview") {
		override val routePattern: String = name
	}

	object BlogPostDetails : Destination("details") {
		const val id = paramId
		override val routePattern: String = "$name/{$id}"
		fun createRoute(id: Int): String = "$name/$id"
	}
}