package com.example.noteyapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.data.db.NoteDatabase
import com.example.noteyapp.feature.home.HomeScreen
import com.example.noteyapp.feature.home.HomeViewModel
import com.example.noteyapp.feature.profile.ProfileScreen
import com.example.noteyapp.feature.profile.ProfileViewModel
import com.example.noteyapp.feature.signin.SignInScreen
import com.example.noteyapp.feature.signup.SignUpScreen
import com.example.noteyapp.ui.theme.NoteyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    database: NoteDatabase,
    dataStoreManager: DataStoreManager
) {
    NoteyTheme {
        val navController = rememberNavController()
        val factory = AppViewModelFactory(database, dataStoreManager)

        NavHost(navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    viewModel = viewModel<HomeViewModel>(factory = factory),
                    navController = navController
                )
            }
            composable("signup") {
                SignUpScreen(
                    navController = navController,
                    dataStoreManager = dataStoreManager
                )
            }
            composable("signin") {
                SignInScreen(
                    navController = navController,
                    dataStoreManager = dataStoreManager
                )
            }
            composable("profile") {
                ProfileScreen(
                    viewModel = viewModel<ProfileViewModel>(factory = factory),
                    navController = navController
                )
            }
        }
    }
}