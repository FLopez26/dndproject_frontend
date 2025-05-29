package com.fls.dndproject_frontend.presentation.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fls.dndproject_frontend.presentation.navigation.Screen
import com.fls.dndproject_frontend.presentation.ui.components.CustomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userId: Int? // Recibe el userId
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(185, 0, 0),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            CustomNavigationBar(
                currentRoute = currentRoute,
                onTabClick = { routeFromCustomNavigationBar ->
                    userId?.let { id ->
                        val targetRoute = when (routeFromCustomNavigationBar) {
                            Screen.MyCharacters.route -> Screen.MyCharacters.createRoute(id)
                            Screen.Forum.route -> Screen.Forum.createRoute(id)
                            Screen.SavedCharacters.route -> Screen.SavedCharacters.createRoute(id)
                            Screen.Profile.route -> Screen.Profile.createRoute(id)
                            else -> routeFromCustomNavigationBar
                        }
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } ?: run {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Contenido del Perfil para Usuario ID: $userId")
        }
    }
}