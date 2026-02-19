package com.example.noteyapp.feature.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.feature.component.LoadingContentSignIn
import com.example.noteyapp.feature.component.NoteyTextField
import com.example.noteyapp.feature.signup.AuthNavigation
import com.example.noteyapp.ui.theme.AccentAmber
import com.example.noteyapp.ui.theme.AccentGold
import com.example.noteyapp.ui.theme.BgCard
import com.example.noteyapp.ui.theme.BgDeep
import com.example.noteyapp.ui.theme.ErrorRed
import com.example.noteyapp.ui.theme.TextMuted
import com.example.noteyapp.ui.theme.TextPrimary
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignInScreen(navController: NavController, dataStoreManager: DataStoreManager) {
    val viewModel = viewModel { SignInViewModel(dataStoreManager) }

    val emailState = viewModel.email.collectAsStateWithLifecycle()
    val passwordState = viewModel.password.collectAsStateWithLifecycle()
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.navigation.collectLatest {
            when (it) {
                is AuthNavigation.NavigateToHome -> {
                    try {
                        navController.getBackStackEntry("home")
                            .savedStateHandle["email"] = emailState.value
                    } catch (_: IllegalArgumentException) {
                    }
                    val popped = navController.popBackStack("home", inclusive = false)
                    if (!popped) {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
        }
    }


    Box(
        modifier = Modifier.fillMaxSize().background(BgDeep)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.radialGradient(
                    colors = listOf(
                        AccentGold.copy(alpha = 0.07f), Color.Transparent
                    ), center = Offset(Float.POSITIVE_INFINITY, 0f), radius = 900f
                )
            )
        )

        when (val currentState = state.value) {
            is SignInState.Loading -> LoadingContentSignIn()

            is SignInState.Failure -> FailureContent(
                error = currentState.error, onRetry = viewModel::onErrorClick
            )

            is SignInState.Success -> SuccessContent(
                email = currentState.response.email,
                onContinue = { viewModel.onSuccessClick(currentState.response.email) })

            is SignInState.Normal -> NormalContent(
                email = emailState.value,
                password = passwordState.value,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onSignIn = viewModel::signIn,
                onSignUpClick = { navController.popBackStack() })
        }
    }
}

@Composable
private fun NormalContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        AnimatedVisibility(
            visible = visible, enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { -30 }) {
            Column {
                Text(
                    text = "notey.",
                    fontSize = 13.sp,
                    letterSpacing = 4.sp,
                    color = AccentGold,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.size(20.dp))
                Text(
                    text = "Welcome\nback.",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 46.sp,
                    color = TextPrimary
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "Sign in to continue your notes.", fontSize = 15.sp, color = TextMuted
                )
            }
        }

        Spacer(Modifier.size(40.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(700, delayMillis = 150)) + slideInVertically(
                tween(
                    700,
                    delayMillis = 150
                )
            ) { 40 }) {
            Column(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                    .background(BgCard).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NoteyTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = "Email",
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            null,
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                NoteyTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Password",
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            null,
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSignIn() })
                )

                Spacer(Modifier.size(4.dp))
                Button(
                    onClick = onSignIn,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGold, contentColor = Color(0xFF0D0D10)
                    )
                ) {
                    Text(
                        text = "Sign In",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(Modifier.size(24.dp))
        AnimatedVisibility(
            visible = visible, enter = fadeIn(tween(800, delayMillis = 300))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New here?", color = TextMuted, fontSize = 14.sp
                )
                TextButton(onClick = onSignUpClick) {
                    Text(
                        text = "Create an account",
                        color = AccentAmber,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun FailureContent(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
                .background(ErrorRed.copy(alpha = 0.12f))
                .padding(horizontal = 24.dp, vertical = 20.dp), contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Sign In Failed",
                    color = ErrorRed,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = error, color = TextMuted, fontSize = 14.sp, textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.size(24.dp))

        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentGold),
            border = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.5f))
        ) {
            Text(text = "Try Again", fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun SuccessContent(email: String, onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "✓", fontSize = 48.sp, color = AccentGold)
        Spacer(Modifier.size(12.dp))
        Text(
            text = "You're in!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary
        )
        Spacer(Modifier.size(6.dp))
        Text(
            text = email, fontSize = 14.sp, color = TextMuted, textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(32.dp))
        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentGold, contentColor = BgDeep
            )
        ) {
            Text(
                text = "Go to Home", fontSize = 15.sp, fontWeight = FontWeight.SemiBold
            )
        }
    }
}