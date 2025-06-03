package com.fls.dndproject_frontend.presentation.viewmodel.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fls.dndproject_frontend.data.repository.ChatRepositoryException
import com.fls.dndproject_frontend.domain.usecase.ollama.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

class ChatViewModel(
    private val chatUseCase: SendMessageUseCase
) : ViewModel() {

    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState

    fun sendUserMessage(message: String) {
        _chatUiState.update { currentState ->
            currentState.copy(
                messages = currentState.messages + ChatMessage(message, MessageRole.USER),
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            try {
                chatUseCase(message).collect { chunk ->
                    _chatUiState.update { currentState ->
                        val updatedMessages = currentState.messages.toMutableList()
                        val lastMessage = updatedMessages.lastOrNull()

                        if (lastMessage?.role == MessageRole.ASSISTANT && !lastMessage.isComplete) {
                            val updatedContent = lastMessage.content + chunk.message.content
                            updatedMessages[updatedMessages.lastIndex] = lastMessage.copy(
                                content = updatedContent,
                                isComplete = chunk.done
                            )
                        } else {
                            updatedMessages.add(
                                ChatMessage(chunk.message.content, MessageRole.ASSISTANT, isComplete = chunk.done)
                            )
                        }
                        currentState.copy(
                            messages = updatedMessages,
                            isLoading = !chunk.done
                        )
                    }
                }
            } catch (e: ChatRepositoryException) {
                _chatUiState.update { it.copy(isLoading = false, error = e.message) }
                e.printStackTrace()
            } catch (e: Exception) {
                _chatUiState.update { it.copy(isLoading = false, error = "Error: ${e.message}") }
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
    val isComplete: Boolean = false
)

enum class MessageRole {
    USER, ASSISTANT
}