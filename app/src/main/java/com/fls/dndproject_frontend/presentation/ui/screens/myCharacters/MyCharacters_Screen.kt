package com.fls.dndproject_frontend.presentation.ui.screens.myCharacters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import com.fls.dndproject_frontend.domain.model.Characters
import com.fls.dndproject_frontend.presentation.navigation.Screen
import com.fls.dndproject_frontend.presentation.ui.components.CustomNavigationBar
import com.fls.dndproject_frontend.presentation.viewmodel.myCharacters.MyCharactersViewModel
import com.fls.dndproject_frontend.presentation.ui.components.MyCharactersCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCharactersScreen(
    navController: NavController,
    userId: Int?,
    myCharactersViewModel: MyCharactersViewModel = koinViewModel()
) {
    val characters by myCharactersViewModel.characters.collectAsState()
    val hasCharacters by myCharactersViewModel.hasCharacters.collectAsState()

    LaunchedEffect(userId) {
        myCharactersViewModel.loadUserDataAndCharacters(userId)
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis Personajes",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(185, 0, 0)
                ),
                actions = {
                    IconButton(onClick = {
                        // TODO: ruta
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "newCharacter",
                            tint = Color.White
                        )
                    }
                }
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
                            else -> {
                                println("DEBUG: CustomNavigationBar passed an unexpected route: $routeFromCustomNavigationBar")
                                routeFromCustomNavigationBar
                            }
                        }
                        println("DEBUG: Navigating to targetRoute: $targetRoute with userId: $id")
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } ?: run {
                        println("Error: userId es null en MyCharactersScreen. Redirigiendo a Login.")
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
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
            when (hasCharacters) {
                true -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(characters) { character ->
                            MyCharactersCard(
                                character = character,
                                onClick = { clickedCharacter: Characters ->
                                    clickedCharacter.characterId?.let { id ->
                                        navController.navigate(Screen.CharacterInfo.createRoute(id))
                                    }
                                }
                            )
                        }
                    }
                }
                false -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¡Parece que no tienes personajes aún!",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Crea el primero y comienza tu aventura.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                        Button(
                            onClick = {
                                // TODO: ruta
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(50.dp),
                            colors = AppStyles.buttonColors
                        ) {
                            Text("Crear mi primer personaje")
                        }
                    }
                }
                //En caso de error y durante la carga de datos
                null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        Text(
                            text = "Cargando . . .",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}