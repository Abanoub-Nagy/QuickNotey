package com.example.noteyapp.feature.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.feature.signup.AuthNavigation
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SignInScreen(navController: NavController, dataStoreManager: DataStoreManager) {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(it)) {
            val viewModel = viewModel {
                SignInViewModel(dataStoreManager)
            }
            val emailState = viewModel.email.collectAsStateWithLifecycle()
            val passwordState = viewModel.password.collectAsStateWithLifecycle()
            val state = viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(true) {
                viewModel.navigation.collectLatest {
                    when (it) {
                        is AuthNavigation.NavigateToHome -> {
                            navController.getBackStackEntry("home").savedStateHandle["email"] =
                                emailState.value
                            navController.popBackStack("home", inclusive = false)
                        }
                    }
                }
            }

            when (state.value) {
                is SignInState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Loading..."
                        )
                    }
                }

                is SignInState.Failure -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Error: ${(state.value as SignInState.Failure).error}")
                        Button(onClick = {
                            viewModel.onErrorClick()
                        }) {
                            Text(text = "Retry")
                        }
                    }
                }

                is SignInState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Sign In Successful: ${(state.value as SignInState.Success).response.email}")
                        Button(onClick = {
                            viewModel.onSuccessClick(
                                (state.value as SignInState.Success).response.email
                            )
                        }) {
                            Text(text = "Go to Home")
                        }
                    }
                }

                is SignInState.Normal -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Sign In", fontSize = 32.sp)

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
                            label = { Text(text = "Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

                        )

                        Spacer(modifier = Modifier.size(16.dp))


                        TextButton({
                            navController.popBackStack()
                        }) {
                            Text(text = "Don't have an account? Sign up")
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Button(
                            onClick = { viewModel.signIn() }, modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Sign In")
                        }
                    }
                }
            }
        }
    }
}