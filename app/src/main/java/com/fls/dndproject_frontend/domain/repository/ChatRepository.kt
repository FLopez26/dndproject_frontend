package com.fls.dndproject_frontend.domain.repository

import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatResponse
import kotlinx.coroutines.flow.Flow


interface ChatRepository {
    suspend fun sendMessage(prompt: String): Flow<OllamaChatResponse>
}