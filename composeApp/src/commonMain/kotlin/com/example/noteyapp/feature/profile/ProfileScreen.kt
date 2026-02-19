package com.example.noteyapp.feature.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.noteyapp.ui.theme.AccentGold
import com.example.noteyapp.ui.theme.BgCard
import com.example.noteyapp.ui.theme.BgDeep
import com.example.noteyapp.ui.theme.BgElevated
import com.example.noteyapp.ui.theme.ErrorBg
import com.example.noteyapp.ui.theme.ErrorRed
import com.example.noteyapp.ui.theme.TextMuted
import com.example.noteyapp.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel, navController: NavController
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    var visible by remember { mutableStateOf(false) }
    var confirmLogout by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier.fillMaxSize().background(BgDeep)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.radialGradient(
                    colors = listOf(
                        AccentGold.copy(alpha = 0.05f), Color.Transparent
                    ), center = Offset(0f, Float.POSITIVE_INFINITY), radius = 700f
                )
            )
        )

        Scaffold(
            containerColor = Color.Transparent, topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Profile",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.2.sp
                        )
                    }, navigationIcon = {
                        Box(
                            modifier = Modifier.padding(start = 12.dp).size(36.dp).clip(CircleShape)
                                .background(BgCard).clickable { navController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    )
                )
            }) { scaffoldPadding ->

            Column(
                modifier = Modifier.fillMaxSize().padding(scaffoldPadding)
                    .padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { -20 }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Outer gold ring
                        Box(
                            modifier = Modifier.size(108.dp).clip(CircleShape).background(
                                Brush.linearGradient(
                                    colors = listOf(AccentGold, AccentGold.copy(alpha = 0.3f))
                                )
                            ), contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier.size(100.dp).clip(CircleShape)
                                    .background(BgCard), contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = AccentGold.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        val displayName = authState.email?.substringBefore("@") ?: "User"
                        Text(
                            text = displayName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = "Member",
                            fontSize = 12.sp,
                            color = AccentGold.copy(alpha = 0.8f),
                            letterSpacing = 2.sp
                        )
                    }
                }

                Spacer(Modifier.height(40.dp))

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600, delayMillis = 150)) + slideInVertically(
                        tween(
                            600, delayMillis = 150
                        )
                    ) { 30 }) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "ACCOUNT",
                            fontSize = 10.sp,
                            letterSpacing = 3.sp,
                            color = AccentGold,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                                .background(BgCard).padding(horizontal = 20.dp, vertical = 18.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                                        .background(BgElevated), contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AlternateEmail,
                                        contentDescription = null,
                                        tint = AccentGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(Modifier.width(14.dp))

                                Column {
                                    Text(
                                        text = "Email Address",
                                        fontSize = 11.sp,
                                        color = TextMuted,
                                        letterSpacing = 0.3.sp
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = authState.email ?: "No email set",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                AnimatedVisibility(
                    visible = visible, enter = fadeIn(tween(700, delayMillis = 250))
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {

                        if (confirmLogout) {
                            // Confirm row
                            Column(
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                                    .background(ErrorBg).padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Sign out of your account?",
                                    fontSize = 14.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Cancel
                                    Box(
                                        modifier = Modifier.weight(1f)
                                            .clip(RoundedCornerShape(12.dp)).background(BgElevated)
                                            .clickable { confirmLogout = false }
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "Cancel",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextMuted
                                        )
                                    }
                                    // Confirm sign out
                                    Box(
                                        modifier = Modifier.weight(1f)
                                            .clip(RoundedCornerShape(12.dp)).background(ErrorRed)
                                            .clickable {
                                                confirmLogout = false
                                                viewModel.logout {
                                                    navController.navigate("signup") {
                                                        popUpTo(0) { inclusive = true }
                                                    }
                                                }
                                            }.padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Sign Out",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxWidth().height(52.dp)
                                    .clip(RoundedCornerShape(14.dp)).background(ErrorBg)
                                    .clickable { confirmLogout = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Logout,
                                        contentDescription = null,
                                        tint = ErrorRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Sign Out",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ErrorRed
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}