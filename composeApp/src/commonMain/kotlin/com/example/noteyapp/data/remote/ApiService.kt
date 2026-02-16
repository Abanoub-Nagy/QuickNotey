package com.example.noteyapp.data.remote

import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.model.AuthRequest
import com.example.noteyapp.model.AuthResponse
import com.example.noteyapp.model.SyncRequest
import com.example.noteyapp.model.SyncResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

expect val BASE_URL_EMULATOR: String
const val BASE_URL = "http://localhost:8080"

class ApiService(
    private val client: HttpClient, private val dataStoreManager: DataStoreManager
) {

    private val ACTIVE_URL = BASE_URL_EMULATOR
    private val LOGIN_ENDPOINT = "$ACTIVE_URL/auth/login"
    private val SIGNUP_ENDPOINT = "$ACTIVE_URL/auth/signup"
    private val SYNC_ENDPOINT = "$ACTIVE_URL/sync"

    // ---------------- Public API ----------------
    suspend fun login(request: AuthRequest): Result<AuthResponse> =
        safePost(LOGIN_ENDPOINT, request)

    suspend fun signup(request: AuthRequest): Result<AuthResponse> =
        safePost(SIGNUP_ENDPOINT, request)

    suspend fun sync(request: SyncRequest): Result<SyncResponse> =
        safePost(SYNC_ENDPOINT, request, authorized = true)

    // ---------------- Generic Safe Post ----------------
    private suspend inline fun <reified T> safePost(
        url: String, body: Any, authorized: Boolean = false
    ): Result<T> {
        return try {
            val response = client.post(url) {
                setBody(body)

                if (authorized) {
                    val token = dataStoreManager.getToken()
                    if (!token.isNullOrBlank()) {
                        header(HttpHeaders.Authorization, "Bearer $token")
                    }
                }
            }

            when (response.status.value) {
                in 200..299 -> Result.success(response.body())
                401 -> Result.failure(ApiError.Unauthorized())
                in 500..599 -> Result.failure(ApiError.Server(response.status.value))
                else -> Result.failure(ApiError.Unknown("HTTP ${response.status.value}"))
            }

        } catch (ex: Exception) {
            Result.failure(ApiError.Network())
        }
    }
}
