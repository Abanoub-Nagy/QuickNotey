package com.example.noteyapp.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

actual object DateUtils {
    @OptIn(markerClass = [ExperimentalTime::class])
    actual fun formatDate(instant: Instant, pattern: String): String {
        val nsDate = NSDate(instant.toEpochMilliseconds()/1000.0)
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = pattern
        return dateFormatter.stringFromDate(nsDate)
    }
}