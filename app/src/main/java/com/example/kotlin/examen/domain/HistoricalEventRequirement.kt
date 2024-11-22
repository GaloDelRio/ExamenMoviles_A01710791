package com.domain

import com.example.kotlin.examen.data.network.model.HistoricalEvent
import com.example.kotlin.examen.data.repositories.HistoricalEventRepository

/**
 * Clase que gestiona la lógica de negocio para obtener eventos históricos a través del repositorio.
 *
 * @param repository Instancia de `HistoricalEventRepository` que se usa para consultar los eventos históricos.
 */
class HistoricalEventRequirement(private val repository: HistoricalEventRepository) {

    /**
     * Consulta los eventos históricos a través del repositorio y pasa los resultados al callback.
     *
     * @param callback Función lambda que recibe dos parámetros:
     * - Una lista de `HistoricalEvent` si la consulta es exitosa, o `null` en caso de error.
     * - Una excepción `Exception` si ocurrió un error, o `null` si fue exitosa.
     */
    fun fetchHistoricalEvents(callback: (List<HistoricalEvent>?, Exception?) -> Unit) {
        // Llama al método del repositorio para obtener los eventos históricos y pasa el resultado al callback
        repository.consultarHistoricalEvents { events, error ->
            callback(events, error) // Devuelve los eventos obtenidos o el error al callback
        }
    }
}
