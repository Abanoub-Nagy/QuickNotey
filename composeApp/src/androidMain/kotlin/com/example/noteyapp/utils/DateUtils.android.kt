package com.example.noteyapp.utils

import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

actual object DateUtils {
    @OptIn(markerClass = [ExperimentalTime::class])
    actual fun formatDate(instant: Instant, pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date = java.util.Date(instant.toEpochMilliseconds())
        return dateFormat.format(date)
    }
}