package com.fls.dndproject_frontend.presentation.ui.screens.myCharacters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.dndproject_frontend.ui.theme.AppStyles
import com.fls.dndproject_frontend.presentation.navigation.Screen
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
    val usernameDisplay by myCharactersViewModel.usernameDisplay.collectAsState()
    val characters by myCharactersViewModel.characters.collectAsState()
    val uiMessage by myCharactersViewModel.uiMessage.collectAsState()
    val hasCharacters by myCharactersViewModel.hasCharacters.collectAsState()

    // No se necesita si no usas SnackBar
    // val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userId) {
        myCharactersViewModel.loadUserDataAndCharacters(userId)
    }

    // No se necesita si no usas SnackBar
    /*
    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            myCharactersViewModel.clearUiMessage()
        }
    }
    */

    Scaffold(
        // No se necesita si no usas SnackBar
        // snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Mis Personajes",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(185, 0, 0)
                ),
                actions = {
                    IconButton(onClick = {
                        // TODO: Aquí pondrás la ruta a la pantalla de creación de personaje
                        // Ejemplo:
                        // navController.navigate(Screen.CreateNewCharacter.route)
                        // Para este ejemplo, volvemos al login, cámbialo por tu ruta real.
                        navController.navigate(Screen.Login.route) { // Esto es un placeholder, cámbialo
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Añadir nuevo personaje",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp), // Ajusta el padding horizontal si es necesario para el grid
            horizontalAlignment = Alignment.CenterHorizontally,
            // Deja el verticalArrangement como Top para que el grid se alinee arriba
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(20.dp)) // Espacio superior

            Text(
                text = "Bienvenido, ${usernameDisplay ?: "..."}",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            when (hasCharacters) {
                true -> {
                    Text(
                        text = "Aquí están tus valiosos compañeros:",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // ¡NUEVO: Usamos LazyVerticalGrid para la cuadrícula de 2 columnas!
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), // ¡Dos columnas!
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp), // Espacio horizontal entre tarjetas
                        verticalArrangement = Arrangement.spacedBy(16.dp), // Espacio vertical entre tarjetas
                        contentPadding = innerPadding // Aplica el padding del Scaffold al contenido del grid
                    ) {
                        items(characters) { character ->
                            MyCharactersCard(
                                character = character,
                                onClick = { //TODO ruta
                                }
                            )
                        }
                    }
                }
                false -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // Hace que ocupe el espacio disponible
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
                            text = "Crea tu primer héroe y comienza tu aventura.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                        Button(
                            onClick = {
                                // TODO: Aquí pondrás la ruta a la pantalla de creación de personaje
                                navController.navigate(Screen.Login.route) { // Placeholder
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
                null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // Hace que ocupe el espacio disponible
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        Text(
                            text = uiMessage ?: "Cargando personajes...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp),
                colors = AppStyles.buttonColors
            ) {
                Text("Volver al Login")
            }
        }
    }
}