package com.fls.dndproject_frontend.presentation.ui.screens.savedCharacters

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fls.dndproject_frontend.presentation.navigation.Screen
import com.fls.dndproject_frontend.presentation.ui.components.CustomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedCharactersScreen(
    navController: NavController,
    userId: Int?
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Personajes guardados",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(185, 0, 0)
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
            Text(text = "Contenido de Personajes Guardados para Usuario ID: $userId")
            // Aquí va el contenido de personajes guardados
        }
    }
}