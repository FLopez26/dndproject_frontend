package com.fls.dndproject_frontend.presentation.navigation

sealed class Screen(val route:String) {
    data object Login : Screen("login")
    data object CreateAccount : Screen("createAccount")
    object MyCharacters : Screen("my_characters_screen/{userId}") {
        fun createRoute(userId: Int) = "my_characters_screen/$userId"
    }
    object CharacterInfo : Screen("characterinfo/{characterId}/{userId}") {
        fun createRoute(characterId: Int, userId: Int) = "characterinfo/$characterId/$userId"
    }
    object Forum : Screen("forum_screen/{userId}") {
        fun createRoute(userId: Int) = "forum_screen/$userId"
    }
    object SavedCharacters : Screen("saved_characters_screen/{userId}") {
        fun createRoute(userId: Int) = "saved_characters_screen/$userId"
    }
    object Profile : Screen("profile_screen/{userId}") {
        fun createRoute(userId: Int) = "profile_screen/$userId"
    }
    object Wizard1 : Screen("wizard1_screen/{userId}") {
        fun createRoute(userId: Int) = "wizard1_screen/$userId"
    }
}