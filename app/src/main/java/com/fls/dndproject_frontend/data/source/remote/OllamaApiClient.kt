package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatRequest
import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.* // O HttpEngineFactory para Multiplatform
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Cliente HTTP para interactuar con la API de Ollama.
 */
class OllamaApiClient(private val baseUrl: String = "http://localhost:11434") {

    private val client = HttpClient(CIO) { // Usamos CIO como motor HTTP
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Ignora campos que no estén en nuestros DTOs
                prettyPrint = false // No necesitas formato bonito para el envío
            })
        }
        // Puedes añadir aquí otros interceptores, timeouts, etc.
    }

    suspend fun chat(request: OllamaChatRequest): OllamaChatResponse {
        return client.post("$baseUrl/api/chat") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    // No olvides cerrar el cliente Ktor cuando ya no lo necesites en una aplicación real
    fun close() {
        client.close()
    }
}