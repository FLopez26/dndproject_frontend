package com.fls.dndproject_frontend.presentation.navigation

sealed class Screen(val route:String) {
    data object Login : Screen("login")
    data object CreateAccount : Screen("createAccount")
    object MyCharacters : Screen("my_characters_screen/{userId}") {
        fun createRoute(userId: Int) = "my_characters_screen/$userId"
    }
    object CharacterInfo : Screen("character_info_screen/{characterId}") {
        fun createRoute(characterId: Int) = "character_info_screen/$characterId"
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
}