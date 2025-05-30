package com.fls.dndproject_frontend.presentation.viewmodel.chat // Asegúrate de que tu paquete sea el correcto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.data.repository.ChatRepositoryException
import com.fls.dndproject_frontend.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update // Para actualizar el StateFlow de forma segura

class ChatViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    // Representa el estado actual del chat (mensajes, estado de carga, errores)
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState

    fun sendUserMessage(message: String) {
        // Añadir el mensaje del usuario a la lista de mensajes
        _chatUiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + ChatMessage(message, MessageRole.USER),
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            try {
                // Iniciar la colección del flujo de respuestas del repositorio
                chatRepository.sendMessage(message).collect { chunk ->
                    // Cada 'chunk' es un OllamaChatResponse que representa un fragmento de la respuesta
                    _chatUiState.update { currentState ->
                        // Busca el último mensaje del asistente para actualizarlo o añadir uno nuevo
                        val updatedMessages = currentState.messages.toMutableList()
                        val lastMessage = updatedMessages.lastOrNull()

                        if (lastMessage?.role == MessageRole.ASSISTANT && !lastMessage.isComplete) {
                            // Si el último mensaje es del asistente y no está completo, lo actualizamos
                            val updatedContent = lastMessage.content + chunk.message.content
                            updatedMessages[updatedMessages.lastIndex] = lastMessage.copy(
                                content = updatedContent,
                                isComplete = chunk.done // Marcar como completo cuando `done` sea true
                            )
                        } else {
                            // Si no hay mensaje del asistente o el último ya está completo, añadir uno nuevo
                            updatedMessages.add(
                                ChatMessage(chunk.message.content, MessageRole.ASSISTANT, isComplete = chunk.done)
                            )
                        }
                        // Actualizar el estado de carga y los mensajes
                        currentState.copy(
                            messages = updatedMessages,
                            isLoading = !chunk.done // isLoading es falso cuando `done` es true
                        )
                    }
                }
            } catch (e: ChatRepositoryException) {
                // Manejar errores de la capa de repositorio
                _chatUiState.update { it.copy(isLoading = false, error = e.message) }
                println("Error en ChatViewModel: ${e.message}")
                e.printStackTrace()
            } catch (e: Exception) {
                // Capturar cualquier otra excepción inesperada
                _chatUiState.update { it.copy(isLoading = false, error = "An unexpected error occurred: ${e.message}") }
                println("Error inesperado en ChatViewModel: ${e.message}")
                e.printStackTrace()
            }
        }
    }
    fun dismissError() {
        _chatUiState.update { currentState ->
            currentState.copy(error = null)
        }
    }
}
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ChatMessage(
    val content: String,
    val role: MessageRole,
    val isComplete: Boolean = false // Indica si la generación del mensaje ha terminado
)

enum class MessageRole {
    USER, ASSISTANT
}