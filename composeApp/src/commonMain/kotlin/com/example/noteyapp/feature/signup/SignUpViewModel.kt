package com.example.noteyapp.feature.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.model.AuthRequest
import com.example.noteyapp.data.remote.ApiService
import com.example.noteyapp.data.remote.HttpClientFactory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val apiService = ApiService(HttpClientFactory.getHttpClient(), dataStoreManager)
    private val _state = MutableStateFlow<SignUpState>(SignUpState.Normal)
    val state = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<AuthNavigation>()
    val navigation = _navigation.asSharedFlow()

    fun onErrorClick() {
        viewModelScope.launch {
            _state.value = SignUpState.Normal
        }
    }

    fun onSuccessClick(email: String) {
        viewModelScope.launch {
            _navigation.emit(AuthNavigation.NavigateToHome(email))
        }
    }

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun signUp() {
        viewModelScope.launch {
            val request = AuthRequest(email.value, password.value)
            _state.value = SignUpState.Loading
            val result = apiService.signup(request)
            if (result.isSuccess) {
                _state.value = SignUpState.Success(result.getOrNull()!!)
                result.getOrNull()?.let {
                    dataStoreManager.storeEmail(it.email)
                    dataStoreManager.storeUserId(it.userId)
                    dataStoreManager.storeRefreshToken(it.refreshToken)
                    dataStoreManager.storeToken(it.accessToken)
                }
            } else {
                _state.value = SignUpState.Failure(result.exceptionOrNull()?.message.toString())
            }
        }
    }
}

sealed class AuthNavigation {
    class NavigateToHome(val email: String) : AuthNavigation()
}