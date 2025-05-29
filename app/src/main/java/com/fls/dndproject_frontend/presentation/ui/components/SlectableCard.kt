package com.fls.dndproject_frontend.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectableCard(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f) // Esto hace que la tarjeta sea cuadrada (ancho = alto)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(185, 0, 0) else Color.LightGray // Rojo si seleccionada, Gris si no
        ),
        border = BorderStroke(2.dp, if (isSelected) Color(100, 0, 0) else Color.Gray) // Borde para mayor contraste
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                color = if (isSelected) Color.White else Color.Black,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(4.dp) // Pequeño padding interno para el texto
            )
        }
    }
}

// --- Preview para SelectableCard ---
@Preview(showBackground = true, widthDp = 180, heightDp = 180)
@Composable
fun SelectableCardPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Tarjetas Seleccionables", style = MaterialTheme.typography.headlineSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SelectableCard(
                name = "Elfo Oscuro",
                isSelected = true,
                onClick = { /* Handle click */ },
                modifier = Modifier.weight(1f)
            )
            SelectableCard(
                name = "Humano",
                isSelected = false,
                onClick = { /* Handle click */ },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SelectableCard(
                name = "Mago",
                isSelected = true,
                onClick = { /* Handle click */ },
                modifier = Modifier.weight(1f)
            )
            SelectableCard(
                name = "Guerrero",
                isSelected = false,
                onClick = { /* Handle click */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}