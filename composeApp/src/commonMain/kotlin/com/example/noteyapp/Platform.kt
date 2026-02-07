package com.example.noteyapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform