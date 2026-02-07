package com.example.noteyapp.feature.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun SignUpScreen(navController: NavController) {
    val viewModel = viewModel<SignUpViewModel>()
    val emailState = viewModel.email.collectAsStateWithLifecycle()
    val passwordState = viewModel.password.collectAsStateWithLifecycle()
    val confirmPasswordState = viewModel.confirmPassword.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up", fontSize = 32.sp)

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            emailState.value,
            onValueChange = {
                viewModel.onEmailChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Email") },
            label = { Text(text = "Email") })

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            passwordState.value,
            onValueChange = {
                viewModel.onPasswordChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Password") },
            label = { Text(text = "Password") })

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            confirmPasswordState.value,
            onValueChange = {
                viewModel.onConfirmPasswordChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Confirm Password") },
            label = { Text(text = "Confirm Password") })

        Spacer(modifier = Modifier.size(16.dp))

        TextButton({
            navController.navigate("signin")
        }){
            Text(text = "Already have an account? Sign in")
        }

        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = { viewModel.signUp() }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Sign Up")
        }
    }
}