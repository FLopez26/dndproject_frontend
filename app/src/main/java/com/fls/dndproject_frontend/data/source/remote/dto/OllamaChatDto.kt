package com.fls.dndproject_frontend.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OllamaApiMessage(
    val role: String, // "user", "assistant", "system"
    val content: String
)

@Serializable
data class OllamaChatRequest(
    val model: String,
    val messages: List<OllamaApiMessage>,
    val stream: Boolean = false // No streaming en este ejemplo
)

@Serializable
data class OllamaChatResponse(
    val model: String,
    val created_at: String,
    val message: OllamaApiMessage,
    val done: Boolean,
    val total_duration: Long? = null,
    val load_duration: Long? = null,
    val prompt_eval_count: Int? = null,
    val prompt_eval_duration: Long? = null,
    val eval_count: Int? = null,
    val eval_duration: Long? = null
)