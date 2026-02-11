package com.example.noteyapp.feature.signin

import com.example.noteyapp.model.AuthResponse

sealed class SignInState {
    object Normal : SignInState()
    object Loading : SignInState()
    class Success(val response: AuthResponse) : SignInState()
    class Failure(val error: String) : SignInState()
}