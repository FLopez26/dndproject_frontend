package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.source.remote.OllamaApiClient
import com.fls.dndproject_frontend.data.source.remote.dto.OllamaApiMessage
import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatRequest
import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatResponse
import com.fls.dndproject_frontend.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl(
    private val ollamaApiClient: OllamaApiClient
) : ChatRepository {

    override suspend fun sendMessage(prompt: String): Flow<OllamaChatResponse> {
        return try {
            val request = OllamaChatRequest(
                model = "llama3.2",
                messages = listOf(
                    OllamaApiMessage(role = "user", content = prompt)
                ),
                stream = true
            )
            ollamaApiClient.chat(request)
        } catch (e: Exception) {
            throw ChatRepositoryException("Error al comunicarse con Ollama: ${e.message}", e)
        }
    }
}

class ChatRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)