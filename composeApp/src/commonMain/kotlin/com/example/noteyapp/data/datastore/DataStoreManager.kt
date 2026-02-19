package com.example.noteyapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val REFRESH_KEY = stringPreferencesKey("refresh")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    // region Flows

    val tokenFlow: Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }
    val emailFlow: Flow<String?> = dataStore.data.map { it[EMAIL_KEY] }
    val refreshTokenFlow: Flow<String?> = dataStore.data.map { it[REFRESH_KEY] }
    val userIdFlow: Flow<String?> = dataStore.data.map { it[USER_ID_KEY] }

    val authStateFlow: Flow<AuthState> = dataStore.data.map { prefs ->
        AuthState(
            token = prefs[TOKEN_KEY],
            email = prefs[EMAIL_KEY],
            refreshToken = prefs[REFRESH_KEY],
            userId = prefs[USER_ID_KEY]
        )
    }

    // endregion

    // region One-shot reads

    suspend fun getToken(): String? = getString(TOKEN_KEY)
    suspend fun getEmail(): String? = getString(EMAIL_KEY)
    suspend fun getRefreshToken(): String? = getString(REFRESH_KEY)
    suspend fun getUserId(): String? = getString(USER_ID_KEY)
    suspend fun getAuthState(): AuthState = authStateFlow.first()

    // endregion

    // region Writes

    suspend fun storeAuthData(
        token: String,
        email: String,
        refreshToken: String,
        userId: String
    ) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[EMAIL_KEY] = email
            prefs[REFRESH_KEY] = refreshToken
            prefs[USER_ID_KEY] = userId
        }
    }

    suspend fun storeToken(token: String) = storeString(TOKEN_KEY, token)
    suspend fun storeEmail(email: String) = storeString(EMAIL_KEY, email)
    suspend fun storeRefreshToken(refreshToken: String) = storeString(REFRESH_KEY, refreshToken)
    suspend fun storeUserId(userId: String) = storeString(USER_ID_KEY, userId)

    // endregion

    // region Clear

    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }

    suspend fun clearToken() = removeKey(TOKEN_KEY)
    suspend fun clearEmail() = removeKey(EMAIL_KEY)
    suspend fun clearRefreshToken() = removeKey(REFRESH_KEY)
    suspend fun clearUserId() = removeKey(USER_ID_KEY)

    // endregion

    // region Private helpers

    private suspend fun getString(key: Preferences.Key<String>): String? =
        dataStore.data.map { it[key] }.first()

    private suspend fun storeString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { it[key] = value }
    }

    private suspend fun removeKey(key: Preferences.Key<String>) {
        dataStore.edit { it.remove(key) }
    }

    // endregion

    // region Models

    data class AuthState(
        val token: String? = null,
        val email: String? = null,
        val refreshToken: String? = null,
        val userId: String? = null
    ) {
        val isLoggedIn: Boolean get() = token != null && userId != null
    }

    // endregion
}