package com.fls.dndproject_frontend.data.mapper

import com.fls.dndproject_frontend.data.source.remote.dto.OllamaApiMessage
import com.fls.dndproject_frontend.domain.model.ChatMessage
import com.fls.dndproject_frontend.domain.model.Sender

fun ChatMessage.toOllamaApiMessage(): OllamaApiMessage {
    val role = when (this.sender) {
        Sender.USER -> "user"
        Sender.BOT -> "assistant"
    }
    return OllamaApiMessage(role = role, content = this.text)
}

fun OllamaApiMessage.toDomainChatMessage(): ChatMessage {
    val sender = when (this.role) {
        "user" -> Sender.USER
        "assistant" -> Sender.BOT
        "system" -> Sender.BOT
        else -> Sender.BOT
    }
    return ChatMessage(text = this.content, sender = sender)
}