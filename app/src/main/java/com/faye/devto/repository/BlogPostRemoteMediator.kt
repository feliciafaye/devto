package com.faye.devto.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import arrow.core.continuations.either
import arrow.core.getOrHandle
import com.faye.devto.database.BlogPostDao
import com.faye.devto.database.BlogPostEntity
import com.faye.devto.network.BlogPostDto
import com.faye.devto.network.DevToClient
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.text.SimpleDateFormat

@OptIn(ExperimentalPagingApi::class)
class BlogPostRemoteMediator(
	private val devtoClient: DevToClient,
	private val blogPostDao: BlogPostDao,
	private val blogPostPagingKeyStore: BlogPostPagingKeyStore
) : RemoteMediator<Int, BlogPostEntity>() {

	override suspend fun initialize(): InitializeAction {
		return if (blogPostPagingKeyStore.nextPage.first() == null) {
			InitializeAction.LAUNCH_INITIAL_REFRESH
		} else {
			InitializeAction.SKIP_INITIAL_REFRESH
		}
	}

	override suspend fun load(loadType: LoadType, state: PagingState<Int, BlogPostEntity>): MediatorResult =
		either<Exception, MediatorResult> {
			when (loadType) {
				LoadType.REFRESH -> blogPostPagingKeyStore.resetNextPage()
				LoadType.PREPEND -> return@either MediatorResult.Success(endOfPaginationReached = true)
				LoadType.APPEND  -> {}
			}

			val page = blogPostPagingKeyStore.nextPage.first()
			Timber.w("Requesting page $page")
			val nextPage = page ?: return@either MediatorResult.Success(endOfPaginationReached = true)

			val posts = devtoClient.getBlogPostsPage(nextPage, state.config.pageSize).bind() // arrow functional library -> either exception or result
			val entities = posts.map { blogPost ->
				blogPost.toBlogPostEntity()
			}
			blogPostDao.storeBlogPosts(entities)
			blogPostPagingKeyStore.saveNextPage(nextPage + 1)

			val endReached = posts.size < state.config.pageSize
			Timber.w("End reached? $endReached")
			MediatorResult.Success(endOfPaginationReached = endReached)
		}.getOrHandle {
			MediatorResult.Error(it) // get result or handle exception
		}
}

private fun BlogPostDto.toBlogPostEntity(): BlogPostEntity =
	BlogPostEntity(
		id = id,
		title = title,
		description = description,
		path = path,
		body = null,
		url = url,
		coverImageUrl = coverImageUrl,
		publishTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(publishedTimestamp)
	)