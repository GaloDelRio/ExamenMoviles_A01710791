package com.example.kotlin.examen.data.network.model

import android.util.Log
import com.example.kotlin.examen.data.network.NetworkModuleDI.callCloudFunction
import com.parse.ParseObject

/**
 * Clase que interactúa con ParseServer para realizar operaciones de red relacionadas con eventos históricos.
 */
class ParseServer {

    /**
     * Recupera una lista de eventos históricos desde un servidor Parse con reintentos en caso de fallos.
     *
     * @param maxRetries Número máximo de intentos permitidos para la llamada (por defecto, 5).
     * @param callback Función lambda que recibe dos parámetros:
     * - Una lista de `HistoricalEvent` si la llamada fue exitosa, o `null` en caso de error.
     * - Una excepción `Exception` si ocurrió un error, o `null` si fue exitosa.
     */
    fun fetchHistoricalEvents(
        maxRetries: Int = 5,
        callback: (List<HistoricalEvent>?, Exception?) -> Unit
    ) {
        val parametros = hashMapOf<String, Any>()

        /**
         * Función interna recursiva que intenta realizar la llamada con reintentos en caso de fallo.
         *
         * @param reintentoActual Número actual de intentos realizados.
         */
        fun intentarLlamada(reintentoActual: Int) {
            // Llama a la función en la nube con los parámetros dados
            callCloudFunction<HashMap<String, Any>>("hello", parametros) { result, error ->
                Log.d("ResultService", result.toString())
                Log.d("ErrorService", error.toString())

                if (error == null && result != null) {
                    try {
                        // Obtiene la lista de ParseObject desde la clave "data"
                        val data = result["data"] as? List<ParseObject>
                        if (data != null) {
                            // Convierte cada ParseObject en un objeto HistoricalEvent
                            val eventos = data.map { parseObject ->
                                HistoricalEvent(
                                    date = parseObject.getString("date") ?: "Unknown",
                                    description = parseObject.getString("description") ?: "No description",
                                    category1 = parseObject.getString("category1") ?: "Unknown",
                                    category2 = parseObject.getString("category2") ?: "Unknown",
                                )
                            }
                            // Devuelve la lista de eventos al callback
                            callback(eventos, null)
                        } else {
                            // Devuelve una lista vacía si no se encontraron datos
                            callback(emptyList(), null)
                        }
                    } catch (e: Exception) {
                        // Maneja excepciones durante el mapeo de datos
                        callback(null, e)
                    }
                } else {
                    // Si ocurre un error y no se alcanzó el máximo de reintentos, intenta nuevamente
                    if (reintentoActual < maxRetries) {
                        Log.w(
                            "RetryService",
                            "Error en intento ${reintentoActual + 1} de $maxRetries. Reintentando..."
                        )
                        intentarLlamada(reintentoActual + 1)
                    } else {
                        // Si se alcanzó el máximo de intentos, pasa el error al callback
                        callback(
                            null,
                            error ?: Exception("Error desconocido después de $maxRetries intentos")
                        )
                    }
                }
            }
        }

        // Inicia la primera llamada
        intentarLlamada(0)
    }
}
