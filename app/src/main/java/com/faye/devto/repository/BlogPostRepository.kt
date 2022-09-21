package com.faye.devto.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.faye.devto.database.BlogPostDao
import com.faye.devto.database.BlogPostEntity
import com.faye.devto.model.BlogPost
import com.faye.devto.network.DevToClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class BlogPostRepository @Inject constructor(
	private val devtoClient: DevToClient,
	private val blogPostsDao: BlogPostDao,
	private val blogPostDao: BlogPostDao,
	private val blogPostsPagingKeyStore: BlogPostPagingKeyStore
) : CoroutineScope {
	@OptIn(ExperimentalPagingApi::class)
	fun blogPosts(pageSize: Int = 10): Flow<PagingData<BlogPost>> =
		Pager(
			config = PagingConfig(
				pageSize = pageSize,
				prefetchDistance = 5 // scrolling to item 5 triggers request
			),
			remoteMediator = BlogPostRemoteMediator(devtoClient, blogPostsDao, blogPostsPagingKeyStore)
		) {
			blogPostsDao.getBlogPostsPaged()
		}
			.flow
			.map { pagingData ->
				pagingData
					.map {
						it.toBlogPost()
					}
			}
			.flowOn(Dispatchers.IO)

	@ExperimentalCoroutinesApi
	fun getBlogPostsFlow(id: Int): Flow<BlogPost> =
		blogPostDao.getBlogPostById(id)
			.filterNotNull()
			.transformLatest { dbPost ->
				emit(dbPost)

				if (dbPost.body == null) {
					devtoClient.getBlogPostText(dbPost.path)
						.map {
							blogPostDao.updatePost(dbPost.copy(body = it))
							emit(dbPost.copy(body = it))
						}
				}
			}
			.map {
				it.toBlogPost()
			}

	override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()
}


private fun BlogPostEntity.toBlogPost(): BlogPost =
	BlogPost(
		id = id,
		title = title,
		description = description,
		path = path,
		body = body,
		url = url,
		coverImageUrl = coverImageUrl,
		publishTimestamp = publishTimestamp
	)


