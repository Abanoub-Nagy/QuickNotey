package com.example.noteyapp.feature.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteyapp.ui.theme.AccentGold
import com.example.noteyapp.ui.theme.TextMuted

@Composable
fun LoadingContentSignUp() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = AccentGold, strokeWidth = 2.dp, modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.size(16.dp))
        Text(
            text = "Creating your account…",
            color = TextMuted,
            fontSize = 14.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun LoadingContentSignIn() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = AccentGold, strokeWidth = 2.dp, modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.size(16.dp))
        Text(
            text = "Signing you in…", color = TextMuted, fontSize = 14.sp, letterSpacing = 0.5.sp
        )
    }
}