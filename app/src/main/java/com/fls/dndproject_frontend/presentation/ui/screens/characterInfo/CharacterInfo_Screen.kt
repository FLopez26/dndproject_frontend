package com.fls.dndproject_frontend.presentation.ui.screens.characterInfo

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.fls.dndproject_frontend.presentation.viewmodel.characterInfo.CharacterInfoViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import com.example.dndproject_frontend.ui.theme.AppStyles.outlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterInfo_Screen(
    navController: NavController,
    characterId: Int?,
    characterInfoViewModel: CharacterInfoViewModel = koinViewModel()
) {
    val character by characterInfoViewModel.character.collectAsState()
    var showPublicDialog  by remember { mutableStateOf(false) }

    LaunchedEffect(characterId) {
        characterId?.let { id ->
            characterInfoViewModel.loadCharacterDetails(id)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = character?.name ?: "Cargando...",
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
                            contentDescription = "Return",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    character?.let {
                        IconButton(onClick = {
                            showPublicDialog = true
                        }) {
                            Icon(
                                imageVector = if (character?.isPublic == true) Icons.Default.Public else Icons.Default.PublicOff,
                                contentDescription = if (character?.isPublic == true) "Hacer privado" else "Hacer público",
                                tint = Color.White
                            )
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (character == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    Text(
                        text = "Cargando detalles del personaje...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                Text(
                    text = "Personaje creado por: ${character?.user?.username}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Separator()
                Headers(text = "Estadísticas")

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Top
                ) {

                    val stats = character?.stats
                    ValueDisplayItem(name = "Fuerza", value = stats?.strength.toString(), modifier = Modifier.weight(1f))
                    ValueDisplayItem(name = "Destreza", value = stats?.dexterity.toString(), modifier = Modifier.weight(1f))
                    ValueDisplayItem(name = "Constitución", value = stats?.constitution.toString(), modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.Top
                ) {

                    val stats = character?.stats
                    ValueDisplayItem(name = "Inteligencia", value = stats?.intelligence.toString(), modifier = Modifier.weight(1f))
                    ValueDisplayItem(name = "Sabiduría", value = stats?.wisdom.toString(), modifier = Modifier.weight(1f))
                    ValueDisplayItem(name = "Carisma", value = stats?.charisma.toString(), modifier = Modifier.weight(1f))
                }

                Separator()
                Headers(text = "Características")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Columna izquierda para HitPoints, DiceHitPoints y Speed
                    Column(
                        modifier = Modifier
                            .weight(0.4f)
                            .padding(end = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ValueDisplayItem(name = "Puntos de Golpe", value = character?.stats?.hitPoints.toString())
                        ValueDisplayItem(name = "Dados de Golpe", value = character?.characterClass?.diceHitPoints)
                        ValueDisplayItem(name = "Velocidad", value = character?.characterRace?.speed.toString())
                    }

                    // Columna derecha para la imagen del personaje
                    Box(
                        modifier = Modifier
                            .weight(0.6f)
                            .height(235.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val imageUrl = character?.image?.toString()
                        if (imageUrl.isNullOrEmpty()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = "Imagen no disponible",
                                    modifier = Modifier.size(64.dp),
                                    tint = Color.Gray
                                )
                                Text("Imagen no disponible", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        } else {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Imagen del personaje ${character?.name}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    //Descripción
                    OutlinedTextField(
                        value = character?.description ?: "Sin descripción",
                        onValueChange = {  },
                        label = { Text("Descripción") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = outlinedTextFieldColors,
                        singleLine = false,
                        maxLines = Int.MAX_VALUE
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Rasgos
                    OutlinedTextField(
                        value = character?.personalityTraits ?: "Sin rasgos de personalidad",
                        onValueChange = {  },
                        label = { Text("Rasgos de personalidad") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = false,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Ideales
                    OutlinedTextField(
                        value = character?.ideals ?: "Sin ideales",
                        onValueChange = {  },
                        label = { Text("Ideales") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = false,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Vínculos
                    OutlinedTextField(
                        value = character?.bonds ?: "Sin vínculos",
                        onValueChange = {  },
                        label = { Text("Vínculos") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = false,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Defectos
                    OutlinedTextField(
                        value = character?.flaws ?: "Sin defectos",
                        onValueChange = {  },
                        label = { Text("Defectos") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = false,
                        maxLines = 5
                    )
                }

                Separator()
                Headers(text = "Rasgos")

                Column(modifier = Modifier.fillMaxWidth()) {
                    //Raza
                    OutlinedTextField(
                        value = character?.characterRace?.name ?: "Sin raza",
                        onValueChange = {  },
                        label = { Text("Raza") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    //Clase
                    OutlinedTextField(
                        value = character?.characterClass?.name ?: "Sin clase",
                        onValueChange = {  },
                        label = { Text("Clase") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    //Trasfondo
                    OutlinedTextField(
                        value = character?.background?.name ?: "Sin trasfondo",
                        onValueChange = {  },
                        label = { Text("Trasfondo") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = true
                    )
                }

                Separator()
                Headers(text = "Otros Datos")

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Abilidades
                    OutlinedTextField(
                        value = (character?.abilities?.race?.abilities + ", " + character?.abilities?.characterClass?.abilities),
                        onValueChange = {  },
                        label = { Text("Rasgos y abilidades") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = false,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Competencias
                    OutlinedTextField(
                        value = (character?.competencies?.characterClass?.competencies + ", " + character?.competencies?.background?.competencies),
                        onValueChange = {  },
                        label = { Text("Competencias") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = false,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Equipamiento
                    OutlinedTextField(
                        value = (character?.equipment?.characterClass?.equipment + ", " + character?.equipment?.background?.equipment),
                        onValueChange = {  },
                        label = { Text("Equipamiento") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors,
                        singleLine = false,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
    if (showPublicDialog) {
        AlertDialog(
            onDismissRequest = {
                showPublicDialog = false
            },
            title = {
                Text(text = if (character?.isPublic == true) "¿Hacer personaje privado?" else "¿Hacer personaje público?")
            },
            text = {
                Text(
                    text = if (character?.isPublic == true)
                        "Al hacer tu personaje privado, dejará de ser visible para otros usuarios."
                    else
                        "Al hacer tu personaje público, será visible para otros usuarios en la aplicación."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPublicDialog = false
                        characterInfoViewModel.updateCharacter()
                    }
                ) {
                    Text(text = if (character?.isPublic == true) "Hacer privado" else "Hacer público")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPublicDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun Separator(){
    Spacer(modifier = Modifier.height(16.dp))
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun Headers(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
        modifier = modifier
            .fillMaxWidth() 
            .padding(start = 4.dp, bottom = 8.dp), 
        textAlign = TextAlign.Start
    )
}

@Composable
fun ValueDisplayItem(name: String, value: String?, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value?.toString() ?: "X",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
