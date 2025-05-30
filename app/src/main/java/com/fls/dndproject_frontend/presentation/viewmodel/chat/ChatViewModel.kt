package com.fls.dndproject_frontend.presentation.viewmodel.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.domain.model.ChatMessage
import com.fls.dndproject_frontend.domain.model.Sender
import com.fls.dndproject_frontend.domain.usecase.ollama.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    // Estado de la lista de mensajes (mutable)
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    // Estado expuesto a la UI (inmutable)
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Estado para indicar si hay una operación en curso (cargando)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para mensajes de error de la UI
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Envía un mensaje del usuario al chatbot.
     * @param text El texto del mensaje del usuario.
     */
    fun sendUserMessage(text: String) {
        if (text.isBlank()) return // No enviar mensajes vacíos
        if (_isLoading.value) return // Evitar múltiples envíos si ya está cargando

        _isLoading.value = true
        _errorMessage.value = null // Limpiar cualquier error anterior

        // Crear el mensaje del usuario y añadirlo inmediatamente a la UI
        val userMessage = ChatMessage(text = text, sender = Sender.USER)
        _messages.update { currentMessages -> currentMessages + userMessage }

        viewModelScope.launch {
            try {
                // El caso de uso maneja la lógica de enviar el mensaje al repositorio
                val botResponse = sendMessageUseCase(userMessage, _messages.value)

                // Añadir la respuesta del bot a la UI
                _messages.update { currentMessages -> currentMessages + botResponse }

            } catch (e: Exception) {
                // Manejar errores: mostrar un mensaje al usuario
                _errorMessage.value = "Error al comunicarse con el bot: ${e.localizedMessage ?: "Desconocido"}. Asegúrate de que Ollama está corriendo y el modelo está descargado."
                println("Error en ChatViewModel: ${e.stackTraceToString()}") // Para depuración
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia el mensaje de error.
     */
    fun dismissError() {
        _errorMessage.value = null
    }

    /**
     * Resetea la conversación (vacía los mensajes).
     */
    fun resetConversation() {
        _messages.value = emptyList()
    }
}