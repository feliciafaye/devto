package com.faye.devto.model

import java.util.*

data class BlogPost(
	val id: Int,
	val title: String,
	val description: String,
	val path: String,
	val body: String?,
	val url: String,
	val coverImageUrl: String?,
	val publishTimestamp: Date?
)