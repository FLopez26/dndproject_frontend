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

// --- NUEVAS IMPORTACIONES PARA EL STREAMING ---
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
// ----------------------------------------------

/**
 * Cliente HTTP para interactuar con la API de Ollama.
 */
class OllamaApiClient(private val baseUrl: String = "http://10.0.2.2:11434") {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = false
                // Añadimos isLenient para mayor tolerancia con el JSON, si fuera necesario
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

    // Definimos el parser JSON aquí para usarlo en la lectura del stream
    // Esto es importante porque ContentNegotiation solo se aplica a un único objeto,
    // y aquí leeremos línea por línea.
    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // --- MÉTODO CHAT MODIFICADO PARA EL STREAMING ---
    suspend fun chat(request: OllamaChatRequest): Flow<OllamaChatResponse> = flow {
        val httpResponse: HttpResponse = client.post("$baseUrl/api/chat") {
            // Asegúrate de que el Content-Type sea JSON para la petición.
            // La respuesta de Ollama será NDJSON si el `stream` es true.
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        // Verificamos que la respuesta sea 200 OK
        if (httpResponse.status.value == 200) {
            // Obtenemos el canal de bytes para leer el cuerpo de la respuesta como un flujo
            val channel: ByteReadChannel = httpResponse.bodyAsChannel()

            // Leemos línea por línea hasta que el canal se cierre
            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line()
                if (line != null && line.isNotBlank()) {
                    try {
                        // Deserializamos cada línea (que es un objeto JSON) a OllamaChatResponse
                        val chatResponse = jsonParser.decodeFromString<OllamaChatResponse>(line)
                        emit(chatResponse) // Emitimos el objeto al Flow
                        if (chatResponse.done) {
                            // Si el campo 'done' es true, el streaming ha terminado
                            break
                        }
                    } catch (e: Exception) {
                        // Registramos cualquier error de parseo pero intentamos continuar
                        // Esto es útil si una línea no es un JSON válido, aunque no debería pasar con Ollama
                        println("Error al parsear línea JSON: $line, Error: ${e.message}")
                        e.printStackTrace() // Para ver el stack trace completo en el log
                    }
                }
            }
        } else {
            // Si el estado HTTP no es 200 OK, leemos el cuerpo como un String
            val errorBody = httpResponse.body<String>()
            throw Exception("La API de Ollama devolvió un estado no 200 OK: ${httpResponse.status.value}, cuerpo: $errorBody")
        }
    }
    // ----------------------------------------------------

    fun close() {
        client.close()
    }
}