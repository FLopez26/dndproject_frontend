package com.fls.dndproject_frontend.presentation.navigation

sealed class Screen(val route:String) {
    data object Login : Screen("login")
    data object CreateAccount : Screen("createAccount")
    object MyCharacters : Screen("my_characters_screen/{userId}") {
        fun createRoute(userId: Int) = "my_characters_screen/$userId"
    }
}