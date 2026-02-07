package com.example.noteyapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteyapp.data.db.NoteDatabase
import com.example.noteyapp.feature.home.HomeScreen
import com.example.noteyapp.feature.signin.SignInScreen
import com.example.noteyapp.feature.signup.SignUpScreen
import com.example.noteyapp.model.Note
import com.example.noteyapp.screen.ListNotesScreen
import com.example.noteyapp.ui.theme.NoteyTheme
import kotlinx.coroutines.launch
import noteyapp.composeapp.generated.resources.Res
import noteyapp.composeapp.generated.resources.empty_logo
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    database: NoteDatabase
) {
    NoteyTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "home"){
            composable(route = "home") {
                HomeScreen(database, navController)
            }
            composable(route = "signup") {
                SignUpScreen(navController)
            }
            composable(route = "signin") {
                SignInScreen(navController)
            }
            composable("profile") {
            }
        }
    }
}