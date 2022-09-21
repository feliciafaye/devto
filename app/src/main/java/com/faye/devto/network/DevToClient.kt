package com.faye.devto.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.faye.devto.coroutines.Io
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DevToClient @Inject constructor(
	private val httpClient: HttpClient,
	@BaseUrl private val baseUrl: String,
	@Io private val coroutineDispatcher: CoroutineDispatcher
) {
	suspend fun getBlogPostsPage(page: Int, pageSize: Int): Either<Exception, List<BlogPostDto>> = withContext(coroutineDispatcher) {
		try {
			val response = httpClient.get("$baseUrl/articles?page=$page&pageSize=$pageSize&type=article")
			if (response.status.value == HttpStatusCode.OK.value) {
				val blogPostDtos = response.body<List<BlogPostDto>>()
				blogPostDtos.right()// the good side of the either
			} else {
				IllegalStateException("Could not load page $page. Response code: ${response.status}").left()// left is the bad side of the either
			}
		} catch (e: Exception) {
			Timber.e(e)
			e.left()
		}
	}

	suspend fun getBlogPostText(path: String): Either<Exception, String> = withContext(coroutineDispatcher) {
		try {
			val response = httpClient.get("$baseUrl/articles/$path")
			if (response.status.value == HttpStatusCode.OK.value) {
				val text = response.body<BlogPostContentDto>().body ?: ""
				text.right()
			} else {
				IllegalStateException("Failed to load character with error code ${response.status.value}").left()
			}
		} catch (e: Exception) {
			Timber.e(e)
			e.left()
		}
	}
}