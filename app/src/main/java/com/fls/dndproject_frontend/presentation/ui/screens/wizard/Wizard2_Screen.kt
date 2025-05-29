// presentation/ui/screens/wizard/Wizard2_Screen.kt
package com.fls.dndproject_frontend.presentation.ui.screens.wizard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
    flaws: String
) {
    val decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8.toString())
    val decodedDescription = URLDecoder.decode(description, StandardCharsets.UTF_8.toString())
    val decodedPersonalityTraits = URLDecoder.decode(personalityTraits, StandardCharsets.UTF_8.toString())
    val decodedIdeals = URLDecoder.decode(ideals, StandardCharsets.UTF_8.toString())
    val decodedBonds = URLDecoder.decode(bonds, StandardCharsets.UTF_8.toString())
    val decodedFlaws = URLDecoder.decode(flaws, StandardCharsets.UTF_8.toString())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Creación de Personaje - Parte 2",
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
                .padding(16.dp)
        ) {
            Text(
                text = "Datos de la primera pantalla:",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text("UserID: $userId")
            Text("Nombre: $decodedName")
            Text("Descripción: $decodedDescription")
            Text("Rasgos: $decodedPersonalityTraits")
            Text("Ideales: $decodedIdeals")
            Text("Vínculos: $decodedBonds")
            Text("Defectos: $decodedFlaws")

            // TODO: Aquí irán los campos y lógica para la Parte 2
            // Por ejemplo: Selección de Raza, Clase, Fondo, etc.
            // Para la siguiente navegación, pasarías estos nuevos campos
            // junto con todos los anteriores (name, description, etc.)
        }
    }
}