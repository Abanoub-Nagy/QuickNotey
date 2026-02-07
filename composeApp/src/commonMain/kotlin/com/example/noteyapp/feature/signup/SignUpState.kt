package com.example.noteyapp.feature.signup

sealed class SignUpState {
    object Normal : SignUpState()
    object Loading : SignUpState()
//    class Success(val response: AuthResponse) : AuthState()
    class Failure(val error: String) : SignUpState()
}