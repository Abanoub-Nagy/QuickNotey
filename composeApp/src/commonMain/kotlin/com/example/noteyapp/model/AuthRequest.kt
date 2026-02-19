package com.example.noteyapp.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String, val password: String
)