package com.example.noteyapp

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteyapp.data.datastore.DataStoreManager
import com.example.noteyapp.data.db.NoteDatabase
import com.example.noteyapp.feature.home.HomeScreen
import com.example.noteyapp.feature.home.HomeViewModel
import com.example.noteyapp.feature.profile.ProfileScreen
import com.example.noteyapp.feature.signin.SignInScreen
import com.example.noteyapp.feature.signup.SignUpScreen
import com.example.noteyapp.ui.theme.NoteyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    database: NoteDatabase, dataStoreManager: DataStoreManager
) {
    NoteyTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "home") {
            composable(route = "home") {
                HomeScreen(
                    database, dataStoreManager, navController
                )
            }
            composable(route = "signup") {
                SignUpScreen(navController, dataStoreManager)
            }
            composable(route = "signin") {
                SignInScreen(navController, dataStoreManager)
            }
            composable("profile") {
                ProfileScreen(
                    dataStoreManager
                )
            }
        }
    }
}