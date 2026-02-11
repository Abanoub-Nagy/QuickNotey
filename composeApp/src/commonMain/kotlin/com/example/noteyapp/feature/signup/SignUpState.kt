package com.example.noteyapp.feature.signup

import com.example.noteyapp.model.AuthResponse

sealed class SignUpState {
    object Normal : SignUpState()
    object Loading : SignUpState()
    class Success(val response: AuthResponse) : SignUpState()
    class Failure(val error: String) : SignUpState()
}