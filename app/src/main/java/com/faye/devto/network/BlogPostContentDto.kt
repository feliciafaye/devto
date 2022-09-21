package com.faye.devto.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlogPostContentDto(
	@SerialName("body_html")
	val body: String?
)