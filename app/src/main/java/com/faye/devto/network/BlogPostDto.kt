package com.faye.devto.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlogPostDto(
	val id: Int,
	val title: String,
	val description: String,
	val path: String,
	val url: String,
	@SerialName("cover_image")
	val coverImageUrl: String? = null,
	@SerialName("published_timestamp")
	val publishedTimestamp: String
)
