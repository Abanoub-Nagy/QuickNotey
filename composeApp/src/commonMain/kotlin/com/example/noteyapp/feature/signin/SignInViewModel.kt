package com.example.noteyapp.feature.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.data.remote.ApiService
import com.example.noteyapp.data.remote.HttpClientFactory
import com.example.noteyapp.feature.signup.AuthNavigation
import com.example.noteyapp.model.AuthRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val apiService = ApiService(HttpClientFactory.getHttpClient())
    private val _state = MutableStateFlow<SignInState>(SignInState.Normal)
    val state = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<AuthNavigation>()
    val navigation = _navigation.asSharedFlow()

    fun onErrorClick() {
        viewModelScope.launch {
            _state.value = SignInState.Normal
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

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }


    fun signIn() {
        viewModelScope.launch {
            val request = AuthRequest(email.value, password.value)
            _state.value = SignInState.Loading
            val result = apiService.login(request)
            if (result.isSuccess) {
                _state.value = SignInState.Success(result.getOrNull()!!)
                result.getOrNull()?.let {
                    dataStoreManager.storeEmail(it.email)
                    dataStoreManager.storeUserId(it.userId)
                    dataStoreManager.storeRefreshToken(it.refreshToken)
                    dataStoreManager.storeToken(it.accessToken)
                }
            } else {
                _state.value = SignInState.Failure(result.exceptionOrNull()?.message.toString())
            }
        }
    }
}
