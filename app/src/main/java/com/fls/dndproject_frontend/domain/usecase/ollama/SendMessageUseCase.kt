package com.fls.dndproject_frontend.domain.usecase.ollama

import com.fls.dndproject_frontend.domain.model.ChatMessage
import com.fls.dndproject_frontend.domain.repository.ChatRepository

class SendMessageUseCase(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(userMessage: ChatMessage, conversationHistory: List<ChatMessage>): ChatMessage {
        // Aquí podrías añadir lógica de negocio adicional antes de enviar,
        // como validaciones, pre-procesamiento del mensaje, etc.
        return chatRepository.sendMessage(userMessage.text, conversationHistory)
    }
}