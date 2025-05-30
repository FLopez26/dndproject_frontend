package com.fls.dndproject_frontend.domain.repository

import com.fls.dndproject_frontend.domain.model.ChatMessage


interface ChatRepository {
    suspend fun sendMessage(prompt: String, conversationHistory: List<ChatMessage>): ChatMessage
}