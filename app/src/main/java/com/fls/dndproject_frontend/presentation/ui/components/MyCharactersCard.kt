package com.fls.dndproject_frontend.presentation.ui.components

import android.graphics.BitmapFactory
import android.util.Base64 // Importa Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fls.dndproject_frontend.domain.model.Characters

@Composable
fun MyCharactersCard(
    character: Characters,
    modifier: Modifier = Modifier,
    onClick: (Characters) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(character) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Verifica si la imagen existe y no está vacía
            if (!character.image.isNullOrEmpty()) {
                val decodedBytes = remember(character.image) {
                    try {
                        Base64.decode(character.image, Base64.DEFAULT)
                    } catch (e: IllegalArgumentException) {
                        println("Error al decodificar Base64: ${e.message}")
                        null
                    }
                }

                val bitmap = remember(decodedBytes) {
                    if (decodedBytes != null) {
                        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    } else {
                        null
                    }
                }

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Imagen de ${character.name}",
                        modifier = Modifier
                            .size(160.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    PlaceholderImage(text = "Error de imagen", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            } else {
                PlaceholderImage(text = "Sin imagen", modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = character.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Raza: ${character.characterRace?.name}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Clase: ${character.characterClass?.name}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

//Imagen que se mostrará si el personaje no tiene una asociada
@Composable
private fun PlaceholderImage(text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .size(160.dp)
            .background(Color.LightGray)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = Color.DarkGray,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}