package com.fls.dndproject_frontend.presentation.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.dndproject_frontend.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.dndproject_frontend.ui.theme.AppStyles.buttonColors
import com.example.dndproject_frontend.ui.theme.AppStyles.outlinedTextFieldColors
import com.example.dndproject_frontend.ui.theme.RedPrimary
import com.fls.dndproject_frontend.presentation.navigation.Screen
import com.fls.dndproject_frontend.presentation.viewmodel.login.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = koinViewModel()
) {
    // Ahora observamos 'username' en lugar de 'email'
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val snackbarMessage by loginViewModel.snackbarMessage.collectAsState()
    val loginSuccess by loginViewModel.loginSuccess.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            loginViewModel.clearSnackbarMessage()
        }
    }

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.navigate(Screen.MyCharacters.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Bienvenido!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Inicio de sesión",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Start
            )

            // CAMBIO AQUÍ: Campo para el Nombre de usuario
            OutlinedTextField(
                value = username,
                onValueChange = { loginViewModel.setUsername(it) }, // Actualiza el username del ViewModel
                label = { Text("Nombre de usuario") }, // Etiqueta del campo
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedTextFieldColors
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { loginViewModel.setPassword(it) },
                label = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val imagen = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = imagen, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedTextFieldColors
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(Screen.CreateAccount.route) }) {
                Text(
                    "Crear cuenta",
                    color = RedPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { loginViewModel.login() },
                modifier = Modifier
                    .align(Alignment.End)
                    .width(130.dp),
                colors = buttonColors
            ) {
                Text("Acceder")
            }
        }
    }
}