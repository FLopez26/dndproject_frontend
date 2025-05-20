package com.fls.dndproject_frontend.presentation.navigation

sealed class Screen(val route:String) {
    data object Login : Screen("login")
    data object CreateAccount : Screen("createAccount")
    data object MyCharacters : Screen("myCharacters")
}