package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.source.remote.OllamaApiClient
import com.fls.dndproject_frontend.data.source.remote.dto.OllamaApiMessage
import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatRequest
import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatResponse
import com.fls.dndproject_frontend.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementación del repositorio para manejar la comunicación con el chat de Ollama.
 */
class ChatRepositoryImpl(
    private val ollamaApiClient: OllamaApiClient
) : ChatRepository { // Asumiendo que ChatRepository es una interfaz que defines

    override suspend fun sendMessage(prompt: String): Flow<OllamaChatResponse> {
        return try {
            val request = OllamaChatRequest(
                model = "llama3.2", // Asegúrate de que este es el modelo que quieres usar
                messages = listOf(
                    OllamaApiMessage(role = "user", content = prompt)
                ),
                stream = true // ¡CRÍTICO! Asegúrate de que `stream` es `true` para recibir NDJSON.
            )
            // Llama al cliente que ahora devuelve un Flow
            ollamaApiClient.chat(request)
        } catch (e: Exception) {
            // Envuelve cualquier excepción del cliente en una excepción de repositorio
            throw ChatRepositoryException("Error al comunicarse con Ollama: ${e.message}", e)
        }
    }
}

// Puedes tener esta excepción definida en otro archivo o aquí mismo si es pequeña
class ChatRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)