package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.mapper.toDomainChatMessage
import com.fls.dndproject_frontend.data.mapper.toOllamaApiMessage
import com.fls.dndproject_frontend.data.source.remote.OllamaApiClient
import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatRequest
import com.fls.dndproject_frontend.domain.model.ChatMessage
import com.fls.dndproject_frontend.domain.model.Sender
import com.fls.dndproject_frontend.domain.repository.ChatRepository

class ChatRepositoryImpl(private val ollamaApiClient: OllamaApiClient) : ChatRepository {

    private val OLLAMA_MODEL = "llama2" // Define el modelo de Ollama que vas a usar

    override suspend fun sendMessage(prompt: String, conversationHistory: List<ChatMessage>): ChatMessage {
        // Convertir el historial de mensajes de dominio a DTOs de Ollama
        val ollamaMessages = conversationHistory.map { it.toOllamaApiMessage() }.toMutableList()

        // Añadir el mensaje actual del usuario al historial para la petición de Ollama
        ollamaMessages.add(ChatMessage(text = prompt, sender = Sender.USER).toOllamaApiMessage())

        // Crear la petición de chat para Ollama
        val request = OllamaChatRequest(
            model = OLLAMA_MODEL,
            messages = ollamaMessages
        )

        try {
            // Realizar la llamada a la API de Ollama
            val response = ollamaApiClient.chat(request)

            // Mapear la respuesta de Ollama a un ChatMessage de dominio
            return response.message.toDomainChatMessage()
        } catch (e: Exception) {
            // Re-lanzar una excepción más amigable o específica de dominio si es necesario
            throw ChatRepositoryException("Error al comunicarse con Ollama: ${e.message}", e)
        }
    }
}

// Excepción personalizada para el repositorio
class ChatRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)