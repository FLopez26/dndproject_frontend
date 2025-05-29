package com.fls.dndproject_frontend.presentation.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
    object Wizard2 : Screen(
        "wizard2_screen/{userId}/" +
                "{name}/{description}/{personalityTraits}/{ideals}/{bonds}/{flaws}"
    ) {
        fun createRoute(
            userId: Int,
            name: String,
            description: String,
            personalityTraits: String,
            ideals: String,
            bonds: String,
            flaws: String
        ): String {
            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
            val encodedDescription = URLEncoder.encode(description, StandardCharsets.UTF_8.toString())
            val encodedPersonalityTraits = URLEncoder.encode(personalityTraits, StandardCharsets.UTF_8.toString())
            val encodedIdeals = URLEncoder.encode(ideals, StandardCharsets.UTF_8.toString())
            val encodedBonds = URLEncoder.encode(bonds, StandardCharsets.UTF_8.toString())
            val encodedFlaws = URLEncoder.encode(flaws, StandardCharsets.UTF_8.toString())

            return "wizard2_screen/$userId/$encodedName/$encodedDescription/" +
                    "$encodedPersonalityTraits/$encodedIdeals/$encodedBonds/$encodedFlaws"
        }
    }
}