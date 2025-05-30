package com.fls.dndproject_frontend.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fls.dndproject_frontend.presentation.ui.screens.characterInfo.CharacterInfo_Screen
import com.fls.dndproject_frontend.presentation.ui.screens.chat.ChatScreen
import com.fls.dndproject_frontend.presentation.ui.screens.createAccount.CreateAccountScreen
import com.fls.dndproject_frontend.presentation.ui.screens.forum.ForumScreen
import com.fls.dndproject_frontend.presentation.ui.screens.login.LoginScreen
import com.fls.dndproject_frontend.presentation.ui.screens.myCharacters.MyCharactersScreen
import com.fls.dndproject_frontend.presentation.ui.screens.profile.ProfileScreen
import com.fls.dndproject_frontend.presentation.ui.screens.savedCharacters.SavedCharactersScreen
import com.fls.dndproject_frontend.presentation.ui.screens.wizard.Wizard1_Screen
import com.fls.dndproject_frontend.presentation.ui.screens.wizard.Wizard2_Screen

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
            arguments = listOf(
                navArgument("characterId") { type = NavType.IntType },
                navArgument("userId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments!!.getInt("characterId")
            val userId = backStackEntry.arguments!!.getInt("userId")

            CharacterInfo_Screen(
                navController = navController,
                characterId = characterId,
                userId = userId
            )
        }
        composable(
            route = Screen.Forum.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            ForumScreen(navController = navController, userId = userId)
        }

        composable(
            route = Screen.SavedCharacters.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            SavedCharactersScreen(navController = navController, userId = userId)
        }

        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            ProfileScreen(navController = navController, userId = userId)
        }

        composable(
            route = Screen.Wizard1.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            Wizard1_Screen(navController = navController, userId = userId)
        }

        composable(
            route = Screen.Wizard2.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("personalityTraits") { type = NavType.StringType },
                navArgument("ideals") { type = NavType.StringType },
                navArgument("bonds") { type = NavType.StringType },
                navArgument("flaws") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val personalityTraits = backStackEntry.arguments?.getString("personalityTraits") ?: ""
            val ideals = backStackEntry.arguments?.getString("ideals") ?: ""
            val bonds = backStackEntry.arguments?.getString("bonds") ?: ""
            val flaws = backStackEntry.arguments?.getString("flaws") ?: ""

            Wizard2_Screen(
                navController = navController,
                userId = userId,
                name = name,
                description = description,
                personalityTraits = personalityTraits,
                ideals = ideals,
                bonds = bonds,
                flaws = flaws
            )
        }

        composable(route = Screen.Chatbot.route) {
            ChatScreen(navController)
        }
    }
}