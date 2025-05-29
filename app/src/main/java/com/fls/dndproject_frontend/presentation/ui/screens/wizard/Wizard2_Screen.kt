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
import com.fls.dndproject_frontend.presentation.viewmodel.wizard.Wizard2ViewModel
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import com.fls.dndproject_frontend.presentation.ui.components.SelectableCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Wizard2_Screen(
    navController: NavController,
    userId: Int?,
    name: String,
    description: String,
    personalityTraits: String,
    ideals: String,
    bonds: String,
    flaws: String,
    viewModel: Wizard2ViewModel = koinViewModel()
) {
    val decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8.toString())
    val decodedDescription = URLDecoder.decode(description, StandardCharsets.UTF_8.toString())
    val decodedPersonalityTraits = URLDecoder.decode(personalityTraits, StandardCharsets.UTF_8.toString())
    val decodedIdeals = URLDecoder.decode(ideals, StandardCharsets.UTF_8.toString())
    val decodedBonds = URLDecoder.decode(bonds, StandardCharsets.UTF_8.toString())
    val decodedFlaws = URLDecoder.decode(flaws, StandardCharsets.UTF_8.toString())

    val races by viewModel.races.collectAsState()
    val classes by viewModel.classes.collectAsState()
    val backgrounds by viewModel.backgrounds.collectAsState()

    val selectedRace by viewModel.selectedRace.collectAsState()
    val selectedClass by viewModel.selectedClass.collectAsState()
    val selectedBackground by viewModel.selectedBackground.collectAsState()

    Scaffold(
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Profundicemos más...",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // --- Sección de Raza ---
                Text(
                    text = "¿Cuál es su raza?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                // Rejilla de Razas
                if (races.isEmpty()) {
                    CircularProgressIndicator(color = Color(185, 0, 0))
                    Text("Cargando razas...", style = MaterialTheme.typography.bodyMedium)
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 3
                    ) {
                        races.forEach { race ->
                            SelectableCard(
                                name = race.name,
                                isSelected = race == selectedRace,
                                onClick = { viewModel.selectRace(race) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                // --- Sección de Clase ---
                Text(
                    text = "¿Y su clase?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                // Rejilla de Clases
                if (classes.isEmpty()) {
                    CircularProgressIndicator(color = Color(185, 0, 0))
                    Text("Cargando clases...", style = MaterialTheme.typography.bodyMedium)
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 3
                    ) {
                        classes.forEach { charClass ->
                            SelectableCard(
                                name = charClass.name,
                                isSelected = charClass == selectedClass,
                                onClick = { viewModel.selectCharacterClass(charClass) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                // --- Sección de Trasfondo ---
                Text(
                    text = "¿Pero cuál será su trasfondo?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                // Rejilla de Trasfondos
                if (backgrounds.isEmpty()) {
                    CircularProgressIndicator(color = Color(185, 0, 0))
                    Text("Cargando trasfondos...", style = MaterialTheme.typography.bodyMedium)
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 3
                    ) {
                        backgrounds.forEach { background ->
                            SelectableCard(
                                name = background.name,
                                isSelected = background == selectedBackground,
                                onClick = { viewModel.selectBackground(background) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))


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
                            // TODO: Implementar lógica de ayuda
                        }
                )
            }

            Button(
                onClick = {//TODO
                },
                enabled = viewModel.areSelectionsComplete(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(Color(185, 0, 0))
            ) {
                Text("Siguiente", color = Color.White)
            }
        }
    }
}