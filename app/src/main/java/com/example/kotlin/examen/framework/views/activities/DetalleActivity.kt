package com.framework.views.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.examen.R

class DetalleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        // Obtener los datos pasados desde el Intent
        val eventDate = intent.getStringExtra("EVENT_DATE")
        val eventDescription = intent.getStringExtra("EVENT_DESCRIPTION")
        val eventCategories = intent.getStringExtra("EVENT_CATEGORIES")

        // Referenciar los TextViews
        val dateTextView: TextView = findViewById(R.id.tvEventDate)
        val descriptionTextView: TextView = findViewById(R.id.tvEventDescription)
        val categoriesTextView: TextView = findViewById(R.id.tvEventCategories)

        // Mostrar los datos en los TextViews
        dateTextView.text = eventDate
        descriptionTextView.text = eventDescription
        categoriesTextView.text = eventCategories

        // Referenciar el botón de regreso
        val backButton: Button = findViewById(R.id.btnBack)

        // Configurar el botón para que regrese a la actividad anterior
        backButton.setOnClickListener {
            finish()  // Cierra la actividad actual y regresa a la anterior
        }
    }
}
