package com.fls.dndproject_frontend.data.mapper

import com.fls.dndproject_frontend.data.source.remote.dto.OllamaApiMessage
import com.fls.dndproject_frontend.domain.model.ChatMessage
import com.fls.dndproject_frontend.domain.model.Sender

fun ChatMessage.toOllamaApiMessage(): OllamaApiMessage {
    val role = when (this.sender) {
        Sender.USER -> "user"
        Sender.BOT -> "assistant" // Ollama usa "assistant" para las respuestas del bot
    }
    return OllamaApiMessage(role = role, content = this.text)
}

// Mapear de OllamaApiMessage (de la respuesta) a ChatMessage de dominio
fun OllamaApiMessage.toDomainChatMessage(): ChatMessage {
    val sender = when (this.role) {
        "user" -> Sender.USER
        "assistant" -> Sender.BOT
        "system" -> Sender.BOT // Consideramos mensajes de sistema como del bot para este ejemplo
        else -> Sender.BOT // Por si acaso hay un rol desconocido
    }
    return ChatMessage(text = this.content, sender = sender)
}