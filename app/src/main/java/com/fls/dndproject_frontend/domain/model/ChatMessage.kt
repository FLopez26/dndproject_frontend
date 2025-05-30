package com.fls.dndproject_frontend.domain.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(), // ID único para el mensaje
    val text: String,
    val sender: Sender, // Quién envió el mensaje (usuario o bot)
    val timestamp: Long = System.currentTimeMillis() // Momento del mensaje
)

enum class Sender {
    USER,
    BOT
}