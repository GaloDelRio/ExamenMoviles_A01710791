package com.framework.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examen.R
import com.example.kotlin.examen.data.network.model.HistoricalEvent
import com.framework.views.activities.DetalleActivity


/**
 * Adaptador para mostrar los eventos históricos en un `RecyclerView`.
 *
 * @param events Lista de eventos históricos que se mostrarán en el `RecyclerView`.
 */
class HistoricalEventAdapter(private var events: List<HistoricalEvent>) :
    RecyclerView.Adapter<HistoricalEventAdapter.ViewHolder>() {

    /**
     * ViewHolder que contiene las vistas de cada item del `RecyclerView`.
     * Se utiliza para optimizar el uso de las vistas en la lista.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Referencias a los elementos de la interfaz de usuario en cada item
        val date: TextView = view.findViewById(R.id.tvDate)
        val description: TextView = view.findViewById(R.id.tvDescription)
        val categories: TextView = view.findViewById(R.id.tvCategories)
    }

    /**
     * Crea un ViewHolder para representar un item en el `RecyclerView`.
     * Se infla el layout de cada item de la lista.
     *
     * @param parent El contenedor donde se insertará el item.
     * @param viewType Tipo de vista, no se usa en este caso.
     * @return Un nuevo `ViewHolder` con las vistas correspondientes.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Infla el layout para cada item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historical_event, parent, false)
        return ViewHolder(view)
    }

    /**
     * Asocia los datos del evento con las vistas correspondientes en cada item.
     *
     * @param holder El `ViewHolder` que contiene las vistas de un item.
     * @param position La posición del item en la lista.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        // Asocia los datos del evento con los elementos de la interfaz de usuario
        holder.date.text = event.date
        holder.description.text = event.description
        holder.categories.text = "${event.category1}, ${event.category2}"

        // Agrega un listener de clic para abrir la nueva actividad
        holder.itemView.setOnClickListener {
            // Crear un Intent para navegar a la nueva actividad
            val intent = Intent(holder.itemView.context, DetalleActivity::class.java)

            // Pasar los datos del evento como extras en el Intent
            intent.putExtra("EVENT_DATE", event.date)
            intent.putExtra("EVENT_DESCRIPTION", event.description)
            intent.putExtra("EVENT_CATEGORIES", "${event.category1}, ${event.category2}")

            // Iniciar la nueva actividad
            holder.itemView.context.startActivity(intent)
        }
    }


    /**
     * Devuelve el número total de items en la lista.
     *
     * @return El número total de eventos históricos en la lista.
     */
    override fun getItemCount(): Int = events.size

    /**
     * Actualiza los datos del adaptador con una nueva lista de eventos.
     * Notifica al `RecyclerView` que los datos han cambiado.
     *
     * @param newEvents Nueva lista de eventos históricos a mostrar.
     */
    fun updateData(newEvents: List<HistoricalEvent>) {
        events = newEvents
        // Refresca el `RecyclerView` para mostrar los nuevos datos
        notifyDataSetChanged()
    }
}
