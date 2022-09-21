package com.faye.devto.database

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
	@TypeConverter
	fun dateToLong(date: Date?): Long? =
		date?.time

	@TypeConverter
	fun longToDate(timestamp: Long?): Date? =
		timestamp?.let {
			Date(it)
		}
}