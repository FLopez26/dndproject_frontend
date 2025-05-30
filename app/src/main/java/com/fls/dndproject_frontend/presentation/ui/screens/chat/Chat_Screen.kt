package com.fls.dndproject_frontend.presentation.ui.screens.chat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fls.dndproject_frontend.data.repository.ChatRepositoryImpl
import com.fls.dndproject_frontend.data.source.remote.OllamaApiClient
import com.fls.dndproject_frontend.domain.model.ChatMessage
import com.fls.dndproject_frontend.domain.model.Sender
import com.fls.dndproject_frontend.domain.usecase.ollama.SendMessageUseCase
import com.fls.dndproject_frontend.presentation.viewmodel.chat.ChatViewModel
import kotlinx.coroutines.launch

// --- Inyección de Dependencias Manual Simple para el ejemplo ---
// En una app real, usarías Koin/Dagger Hilt.
class AppContainer {
    val ollamaApiClient = OllamaApiClient()
    val chatRepository = ChatRepositoryImpl(ollamaApiClient)
    val sendMessageUseCase = SendMessageUseCase(chatRepository)
}
val appContainer = AppContainer()
// --- Fin Inyección de Dependencias Manual Simple ---

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Puedes usar tu tema aquí
            MaterialTheme { // O tu MyChatbotAppTheme
                ChatScreen(appContainer.sendMessageUseCase)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(sendMessageUseCase: SendMessageUseCase) {
    val viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(sendMessageUseCase))
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Desplazarse al último mensaje cuando la lista cambie
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(messages.lastIndex)
            }
        }
    }

    // Mostrar Toast para errores
    errorMessage?.let { error ->
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        viewModel.dismissError() // Limpiar el error después de mostrarlo
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ollama Chatbot") })
        },
        bottomBar = {
            MessageInput(
                inputText = inputText,
                onInputChange = { inputText = it },
                onSendMessage = {
                    viewModel.sendUserMessage(inputText)
                    inputText = "" // Limpiar el campo de texto después de enviar
                },
                isLoading = isLoading
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
            if (isLoading) {
                item {
                    // Indicador de que el bot está escribiendo
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
                            modifier = Modifier.padding(end = 60.dp)
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

@Composable
fun ChatMessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.sender == Sender.USER) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (message.sender) {
                    Sender.USER -> MaterialTheme.colorScheme.primaryContainer
                    Sender.BOT -> MaterialTheme.colorScheme.secondaryContainer
                }
            ),
            modifier = Modifier.widthIn(max = 300.dp) // Limita el ancho de la burbuja
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(8.dp),
                color = when (message.sender) {
                    Sender.USER -> MaterialTheme.colorScheme.onPrimaryContainer
                    Sender.BOT -> MaterialTheme.colorScheme.onSecondaryContainer
                }
            )
        }
    }
}

@Composable
fun MessageInput(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre elementos
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = onInputChange,
            label = { Text("Escribe un mensaje...") },
            modifier = Modifier.weight(1f),
            singleLine = false,
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading // Deshabilita mientras está cargando
        )
        // Usamos IconButton en lugar de FloatingActionButton para el icono de envío
        // Esto permite un control más directo del estado 'enabled'
        IconButton(
            onClick = onSendMessage,
            enabled = inputText.isNotBlank() && !isLoading, // Aquí 'enabled' funciona directamente
            modifier = Modifier
                .size(56.dp) // Puedes ajustar el tamaño si quieres que se parezca más a un FAB
                .background(
                    color = MaterialTheme.colorScheme.primary, // Color de fondo del botón
                    shape = RoundedCornerShape(28.dp) // Forma circular para que se vea como un FAB
                )
        ) {
            Icon(
                imageVector = Icons.Filled.Send, // Asegúrate de que sea 'imageVector'
                contentDescription = "Enviar mensaje",
                tint = MaterialTheme.colorScheme.onPrimary // Color del icono
            )
        }
    }
}

// Factoría simple para ViewModel
class ChatViewModelFactory(
    private val sendMessageUseCase: SendMessageUseCase
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(sendMessageUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}