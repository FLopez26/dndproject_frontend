package com.fls.dndproject_frontend.presentation.ui.screens.chat

import android.widget.Toast
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fls.dndproject_frontend.presentation.viewmodel.chat.ChatViewModel
// Importa las nuevas clases de estado del ViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.chat.ChatMessage // <-- Asegúrate de que esta sea la ruta correcta
import com.fls.dndproject_frontend.presentation.viewmodel.chat.MessageRole // <-- Asegúrate de que esta sea la ruta correcta

import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * Composable que representa la pantalla de chat.
 * Recibe el NavController para manejar la navegación y obtiene su ViewModel con Koin.
 *
 * @param navController El NavController para gestionar la navegación dentro de la app.
 * @param chatViewModel El ViewModel de la pantalla de chat, inyectado por Koin por defecto.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatViewModel: ChatViewModel = koinViewModel()
) {
    // --- CAMBIOS CLAVE AQUÍ: Observar el chatUiState completo ---
    val uiState by chatViewModel.chatUiState.collectAsState()
    // Ya no necesitas 'messages', 'isLoading', 'errorMessage' por separado
    // Accede a ellos directamente desde uiState
    val messages = uiState.messages
    val isLoading = uiState.isLoading
    val errorMessage = uiState.error // Cambiado a 'error' según el ViewModel

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Define el color principal
    val dndRed = Color(185, 0, 0) // RGB(185, 0, 0)

    // Ajusta LaunchedEffect para reaccionar a cambios en messages y scrollear al final
    // Es importante que el scroll solo se dispare cuando se añade un mensaje, no en cada token.
    // Una forma de hacerlo es observando el tamaño de la lista de mensajes.
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scope.launch {
                // Scrollear al último elemento para mostrar el mensaje más reciente
                listState.animateScrollToItem(messages.lastIndex)
            }
        }
    }

    // Mostrar el Toast para errores
    uiState.error?.let { error ->
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        chatViewModel.dismissError() // Si tienes un método para limpiar el error en el ViewModel
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ollama Chatbot (D&D)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = dndRed,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            MessageInput(
                inputText = inputText,
                onInputChange = { inputText = it },
                onSendMessage = {
                    chatViewModel.sendUserMessage(inputText)
                    inputText = ""
                },
                isLoading = isLoading, // Pasa el estado de carga al input
                accentColor = dndRed
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(messages) { message ->
                ChatMessageBubble(message = message)
            }
            // Muestra el indicador de "escribiendo" solo si el bot está cargando
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart // Los mensajes del bot inician a la izquierda
                    ) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
                            modifier = Modifier.padding(end = 60.dp) // Deja espacio en el lado derecho
                        ) {
                            Text(
                                text = "Bot escribiendo...",
                                modifier = Modifier.padding(8.dp),
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Composable para la burbuja de mensaje (AJUSTADO: `message.sender` ahora es `MessageRole`) ---
@Composable
fun ChatMessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.role == MessageRole.USER) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (message.role) { // <-- Usa message.role
                    MessageRole.USER -> MaterialTheme.colorScheme.primaryContainer
                    MessageRole.ASSISTANT -> MaterialTheme.colorScheme.secondaryContainer // <-- Usa MessageRole.ASSISTANT
                }
            ),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.content, // <-- Usa message.content
                modifier = Modifier.padding(8.dp),
                color = when (message.role) { // <-- Usa message.role
                    MessageRole.USER -> MaterialTheme.colorScheme.onPrimaryContainer
                    MessageRole.ASSISTANT -> MaterialTheme.colorScheme.onSecondaryContainer // <-- Usa MessageRole.ASSISTANT
                }
            )
        }
    }
}

// --- Composable para la entrada de mensaje (sin cambios, ya era correcto) ---
@Composable
fun MessageInput(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isLoading: Boolean,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = onInputChange,
            label = { Text("Escribe un mensaje...") },
            modifier = Modifier.weight(1f),
            singleLine = false,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading // Deshabilita la entrada si el bot está cargando
        )
        IconButton(
            onClick = onSendMessage,
            enabled = inputText.isNotBlank() && !isLoading, // Deshabilita el botón si el bot está cargando o el texto está vacío
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = accentColor,
                    shape = RoundedCornerShape(28.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Enviar mensaje",
                tint = Color.White
            )
        }
    }
}