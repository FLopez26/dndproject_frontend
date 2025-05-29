// src/main/java/com/fls/dndproject_frontend/presentation/ui/screens/forum/ForumScreen.kt
package com.fls.dndproject_frontend.presentation.ui.screens.forum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dndproject_frontend.ui.theme.AppStyles
import com.fls.dndproject_frontend.presentation.navigation.Screen
import com.fls.dndproject_frontend.presentation.ui.components.CustomNavigationBar
import com.fls.dndproject_frontend.presentation.ui.components.MyCharactersCard
import com.fls.dndproject_frontend.presentation.viewmodel.forum.ForumViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    navController: NavController,
    userId: Int?,
    forumViewModel: ForumViewModel = koinViewModel()
) {
    val publicCharacters by forumViewModel.publicCharacters.collectAsState()
    val isLoading by forumViewModel.isLoading.collectAsState() // Observa el nuevo estado de carga
    val hasPublicCharacters by forumViewModel.hasPublicCharacters.collectAsState()

    LaunchedEffect(Unit) {
        // forumViewModel.loadPublicCharacters() // Ya se llama en el init del ViewModel
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Personajes Públicos",
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
                onTabClick = { route ->
                    userId?.let { id ->
                        val targetRoute = when (route) {
                            Screen.MyCharacters.route -> Screen.MyCharacters.createRoute(id)
                            Screen.Forum.route -> Screen.Forum.createRoute(id)
                            Screen.SavedCharacters.route -> Screen.SavedCharacters.createRoute(id)
                            Screen.Profile.route -> Screen.Profile.createRoute(id)
                            else -> route
                        }
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            when {
                isLoading -> { // Si está cargando
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        Text(
                            text = "Cargando personajes públicos . . .",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                hasPublicCharacters -> { // Si hay personajes y no está cargando
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(publicCharacters) { character ->
                            MyCharactersCard(
                                character = character,
                                onClick = { clickedCharacter ->
                                    clickedCharacter.characterId?.let { id ->
                                        userId?.let { loggedInUserId ->
                                            navController.navigate(Screen.CharacterInfo.createRoute(id, loggedInUserId))
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
                else -> { // Si no hay personajes y no está cargando
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¡Upps! Parece que no hay personajes públicos disponibles.",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Sé el primero en compartir tu personaje o vuelve más tarde.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                    }
                }
            }
        }
    }
}