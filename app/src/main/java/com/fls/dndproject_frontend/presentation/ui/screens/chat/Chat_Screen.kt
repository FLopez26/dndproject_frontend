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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fls.dndproject_frontend.presentation.viewmodel.chat.ChatViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.chat.ChatMessage
import com.fls.dndproject_frontend.presentation.viewmodel.chat.MessageRole

import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    chatViewModel: ChatViewModel = koinViewModel()
) {
    val uiState by chatViewModel.chatUiState.collectAsState()
    val messages = uiState.messages
    val isLoading = uiState.isLoading

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(messages.lastIndex)
            }
        }
    }

    uiState.error?.let { error ->
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        chatViewModel.dismissError()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Chat de Ayuda",
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
        },
        bottomBar = {
            MessageInput(
                inputText = inputText,
                onInputChange = { inputText = it },
                onSendMessage = {
                    chatViewModel.sendUserMessage(inputText)
                    inputText = ""
                },
                isLoading = isLoading,
                accentColor = Color(185, 0, 0)
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
        horizontalArrangement = if (message.role == MessageRole.USER) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (message.role) {
                    MessageRole.USER -> MaterialTheme.colorScheme.primaryContainer
                    MessageRole.ASSISTANT -> MaterialTheme.colorScheme.secondaryContainer
                }
            ),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(8.dp),
                color = when (message.role) {
                    MessageRole.USER -> MaterialTheme.colorScheme.onPrimaryContainer
                    MessageRole.ASSISTANT -> MaterialTheme.colorScheme.onSecondaryContainer
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
            enabled = !isLoading
        )
        IconButton(
            onClick = onSendMessage,
            enabled = inputText.isNotBlank() && !isLoading,
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