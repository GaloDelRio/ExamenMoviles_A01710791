package com.example.kotlin.examen.data.repositories

import com.example.kotlin.examen.data.network.model.HistoricalEvent
import com.example.kotlin.examen.data.network.model.ParseServer

/**
 * Repositorio que gestiona la obtención de eventos históricos desde el servidor.
 *
 * @param service Instancia de `ParseServer` que se utiliza para hacer las peticiones de red.
 */
class HistoricalEventRepository(private val service: ParseServer) {

    /**
     * Consulta los eventos históricos llamando al servicio `ParseServer` y pasa los resultados al callback.
     *
     * @param callback Función lambda que recibe dos parámetros:
     * - Una lista de `HistoricalEvent` si la consulta es exitosa, o `null` en caso de error.
     * - Una excepción `Exception` si ocurrió un error, o `null` si fue exitosa.
     */
    fun consultarHistoricalEvents(callback: (List<HistoricalEvent>?, Exception?) -> Unit) {
        // Llama al servicio para obtener los eventos históricos y pasa el resultado al callback
        service.fetchHistoricalEvents { events, error ->
            callback(events, error) // Devuelve los eventos obtenidos o el error al callback
        }
    }
}
