package com.fls.dndproject_frontend.presentation.ui.screens.createAccount

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dndproject_frontend.ui.theme.AppStyles

@Composable
fun CreateAccountScreen(
    navController: NavController,
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Column(
            Modifier.padding(innerPadding)
        ) {
            Text(
                modifier = Modifier
                    .padding(30.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Crea una cuenta \ny comienza tu aventura",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally),
                colors = AppStyles.outlinedTextFieldColors
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally),
                colors = AppStyles.outlinedTextFieldColors
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val imagen = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = imagen, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally),
                colors = AppStyles.outlinedTextFieldColors
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirme su contraseña") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val imagen = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = imagen, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally),
                colors = AppStyles.outlinedTextFieldColors
            )

            Button(
                onClick = { /* TODO: Implementar lógica de creación de cuenta */ },
                modifier = Modifier
                    .align(Alignment.End)
                    .width(130.dp)
                    .padding(30.dp),
                colors = AppStyles.buttonColors
            ) {
                Text("Comenzar")
            }

        }
    }
}