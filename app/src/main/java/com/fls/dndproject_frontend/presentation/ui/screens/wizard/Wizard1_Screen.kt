// presentation/ui/screens/wizard/Wizard1_Screen.kt
package com.fls.dndproject_frontend.presentation.ui.screens.wizard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import com.example.dndproject_frontend.ui.theme.AppStyles.outlinedTextFieldColors
import com.fls.dndproject_frontend.presentation.viewmodel.wizard.Wizard1ViewModel
import com.fls.dndproject_frontend.presentation.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Wizard1_Screen(
    navController: NavController,
    userId: Int?,
    viewModel: Wizard1ViewModel = koinViewModel()
) {
    val name by viewModel.characterName.collectAsState()
    val description by viewModel.description.collectAsState()
    val personalityTraits by viewModel.personalityTraits.collectAsState()
    val ideals by viewModel.ideals.collectAsState()
    val bonds by viewModel.bonds.collectAsState()
    val flaws by viewModel.flaws.collectAsState()

    val message by viewModel.message.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.messageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Creación de Personaje",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        maxLines = 1
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(185, 0, 0)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Para comenzar, dale un sentido a tu personaje",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.setCharacterName(it) },
                    label = { Text("Nombre del Personaje") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors,
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { viewModel.setDescription(it) },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors,
                    singleLine = false,
                    // maxLines = 5
                )

                OutlinedTextField(
                    value = personalityTraits,
                    onValueChange = { viewModel.setPersonalityTraits(it) },
                    label = { Text("Rasgos de personalidad") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors,
                    singleLine = false,
                    // maxLines = 5
                )

                OutlinedTextField(
                    value = ideals,
                    onValueChange = { viewModel.setIdeals(it) },
                    label = { Text("Ideales") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors,
                    singleLine = false,
                    // maxLines = 5
                )

                OutlinedTextField(
                    value = bonds,
                    onValueChange = { viewModel.setBonds(it) },
                    label = { Text("Vínculos") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors,
                    singleLine = false,
                    // maxLines = 5
                )

                OutlinedTextField(
                    value = flaws,
                    onValueChange = { viewModel.setFlaws(it) },
                    label = { Text("Defectos") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors,
                    singleLine = false,
                    // maxLines = 5
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¿Necesitas ayuda?",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp)
                        .clickable {
                            println("DEBUG: Se hizo clic en '¿Necesitas ayuda?'")
                        }
                )
            }

            Button(
                onClick = {
                    if (viewModel.validateAndProceed()) {
                        navController.navigate(
                            Screen.Wizard2.createRoute(
                                userId = userId ?: 0,
                                name = name,
                                description = description,
                                personalityTraits = personalityTraits,
                                ideals = ideals,
                                bonds = bonds,
                                flaws = flaws
                            )
                        )
                    } else {
                        println("DEBUG: Validación fallida en la primera pantalla.")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(185, 0, 0))
            ) {
                Text("Siguiente", color = Color.White)
            }
        }
    }

}