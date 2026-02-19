package com.example.noteyapp.utils

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

expect object DateUtils {

    @OptIn(ExperimentalTime::class)
    fun formatDate(instant: Instant, pattern: String = "yyyy-MM-dd HH:mm:ss"): String
}