package com.framework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domain.HistoricalEventRequirement
import com.example.kotlin.examen.data.network.model.HistoricalEvent

/**
 * ViewModel encargado de gestionar los eventos históricos y proporcionar los datos
 * a la vista (UI) en un formato observables.
 *
 * @param requirement Instancia de `HistoricalEventRequirement` utilizada para recuperar los eventos históricos.
 */
class HistoricalEventViewModel(private val requirement: HistoricalEventRequirement) : ViewModel() {

    /**
     * LiveData privada que mantiene la lista de eventos históricos recuperados.
     * La lista de eventos es observable por la vista para actualizar la UI cuando cambian.
     */
    private val _events = MutableLiveData<List<HistoricalEvent>?>()

    /**
     * LiveData pública para exponer la lista de eventos históricos a la vista (UI).
     * La vista puede observar esta propiedad para recibir actualizaciones de los eventos.
     */
    val events: MutableLiveData<List<HistoricalEvent>?> get() = _events

    /**
     * LiveData privada que contiene el mensaje de error.
     * Se utiliza para transmitir errores a la UI en caso de que ocurra algún problema.
     */
    private val _error = MutableLiveData<String?>()

    /**
     * LiveData pública que expone el mensaje de error a la vista (UI).
     * La UI puede observar este LiveData para mostrar el error al usuario.
     */
    val error: LiveData<String?> get() = _error

    /**
     * Método que solicita los eventos históricos a través del `HistoricalEventRequirement`.
     * Una vez que los datos se obtienen o si ocurre un error, actualiza las propiedades `events` o `error`.
     */
    fun recuperarEventos() {
        // Llamada al método para obtener los eventos históricos
        requirement.fetchHistoricalEvents { events, error ->
            if (error == null && events != null) {
                // Si la llamada es exitosa, se actualizan los eventos en LiveData
                _events.postValue(events)
            } else {
                // Si ocurre un error, se actualiza el mensaje de error en LiveData
                _error.postValue(error?.localizedMessage ?: "Unknown error")
            }
        }
    }

    /**
     * Método que aplica un filtro a la lista de eventos históricos según el texto proporcionado.
     * Si el texto está vacío, se muestran todos los eventos.
     * Si no, se filtran los eventos por nombre o cualquier otra propiedad relevante.
     *
     * @param filterText El texto de filtro que se usará para filtrar los eventos.
     */
    fun applyFilter(filterText: String) {
        // Obtén la lista de eventos actuales (si existe) y aplícales el filtro
        val filteredEvents = _events.value?.filter {
            it.date.contains(filterText, ignoreCase = true) // Filtra por nombre o cualquier otra propiedad
        }

        // Si el texto de filtro está vacío, muestra todos los eventos
        if (filterText.isBlank()) {
            // Si el filtro está vacío, muestra todos los eventos
            _events.value = _events.value
        } else {
            // Actualiza el LiveData con los eventos filtrados
            _events.value = filteredEvents
        }
    }
}
