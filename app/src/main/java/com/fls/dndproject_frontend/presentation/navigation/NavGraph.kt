package com.fls.dndproject_frontend.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fls.dndproject_frontend.presentation.ui.screens.createAccount.CreateAccountScreen
import com.fls.dndproject_frontend.presentation.ui.screens.login.LoginScreen
import com.fls.dndproject_frontend.presentation.ui.screens.myCharacters.MyCharactersScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.CreateAccount.route) {
            CreateAccountScreen(navController)
        }
        composable(route = Screen.MyCharacters.route ) {
            MyCharactersScreen(navController)
        }
    }
}