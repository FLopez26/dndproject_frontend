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
import com.example.dndproject_frontend.ui.theme.AppStyles.buttonColors
import com.example.dndproject_frontend.ui.theme.AppStyles.outlinedTextFieldColors
import com.example.dndproject_frontend.ui.theme.RedPrimary
import com.fls.dndproject_frontend.presentation.navigation.Screen
import com.fls.dndproject_frontend.presentation.viewmodel.createAccount.CreateAccountViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(
    navController: NavController,
    createAccountViewModel: CreateAccountViewModel = koinViewModel()
) {
    val username by createAccountViewModel.username.collectAsState()
    val email by createAccountViewModel.email.collectAsState()
    val password by createAccountViewModel.password.collectAsState()
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val message by createAccountViewModel.message.collectAsState()
    val accountCreatedSuccessfully by createAccountViewModel.accountCreatedSuccessfully.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(accountCreatedSuccessfully) {
        if (accountCreatedSuccessfully) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.CreateAccount.route) { inclusive = true }
            }
            createAccountViewModel.resetAccountCreationSuccessState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            Modifier.padding(innerPadding)
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Crea una cuenta \ny comienza tu aventura",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Spacer(modifier = Modifier.height(90.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { createAccountViewModel.setUsername(it) },
                maxLines = 1,
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedTextFieldColors
            )
            Spacer(modifier = Modifier.height(25.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { createAccountViewModel.setEmail(it) },
                maxLines = 1,
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedTextFieldColors
            )
            Spacer(modifier = Modifier.height(25.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { createAccountViewModel.setPassword(it) },
                maxLines = 1,
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
            Spacer(modifier = Modifier.height(25.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                maxLines = 1,
                label = { Text("Repita su contraseña") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val imagen = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = imagen, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedTextFieldColors
            )
            Spacer(modifier = Modifier.height(25.dp))

            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text(
                    "Volver",
                    color = RedPrimary
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = {
                    createAccountViewModel.createAccount(confirmPassword)
                    if(message == "Cuenta creada exitosamente."){
                        navController.navigate(Screen.Login.route)
                    }},
                modifier = Modifier
                    .align(Alignment.End)
                    .width(130.dp),
                colors = buttonColors
            ) {
                Text("Crear")
            }
        }
    }
}