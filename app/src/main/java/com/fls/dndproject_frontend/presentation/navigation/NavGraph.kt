package com.fls.dndproject_frontend.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fls.dndproject_frontend.presentation.ui.screens.characterInfo.CharacterInfo_Screen
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
        composable(
            route = Screen.MyCharacters.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            MyCharactersScreen(navController = navController, userId = userId)
        }
        composable(
            route = Screen.CharacterInfo.route,
            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId")
            CharacterInfo_Screen(navController = navController, characterId = characterId)
        }
    }
}