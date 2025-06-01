package com.fls.dndproject_frontend.data.source.remote

import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatRequest
import com.fls.dndproject_frontend.data.source.remote.dto.OllamaChatResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OllamaApiClient(private val baseUrl: String = "http://10.0.2.2:11434") {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = false
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.BODY
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 60000
            socketTimeoutMillis = 60000
        }
    }

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun chat(request: OllamaChatRequest): Flow<OllamaChatResponse> = flow {
        val httpResponse: HttpResponse = client.post("$baseUrl/api/chat") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (httpResponse.status.value == 200) {
            val channel: ByteReadChannel = httpResponse.bodyAsChannel()

            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line()
                if (line != null && line.isNotBlank()) {
                    try {
                        val chatResponse = jsonParser.decodeFromString<OllamaChatResponse>(line)
                        emit(chatResponse)
                        if (chatResponse.done) {
                            break
                        }
                    } catch (e: Exception) {
                        println("Error con el JSON: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
        } else {
            val errorBody = httpResponse.body<String>()
            throw Exception("Mal estado: ${httpResponse.status.value}, cuerpo: $errorBody")
        }
    }
}