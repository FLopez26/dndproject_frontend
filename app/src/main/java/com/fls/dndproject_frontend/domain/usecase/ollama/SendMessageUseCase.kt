package com.fls.dndproject_frontend.domain.usecase.ollama

import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatResponse // Importa OllamaChatResponse
import com.fls.dndproject_frontend.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow // Importa Flow

class SendMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(userMessageContent: String): Flow<OllamaChatResponse> {
        return chatRepository.sendMessage(userMessageContent)
    }
}