package com.framework.views.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.domain.HistoricalEventRequirement
import com.example.kotlin.examen.data.network.model.ParseServer
import com.example.kotlin.examen.data.repositories.HistoricalEventRepository
import com.example.kotlin.examen.databinding.ActivityMainBinding
import com.framework.adapter.HistoricalEventAdapter
import com.framework.viewmodel.HistoricalEventViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var historicalEventViewModel: HistoricalEventViewModel
    private lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var historicalEventAdapter: HistoricalEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeBindings()
        initializeViewModel()
        initializeRecyclerView()
        initializeObservers()
        setupSearch()
        historicalEventViewModel.recuperarEventos()
    }

    private fun initializeBindings() {
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
    }

    private fun initializeViewModel() {
        val parseService = ParseServer()
        val historicalEventRepository = HistoricalEventRepository(parseService)
        val historicalEventRequirement = HistoricalEventRequirement(historicalEventRepository)
        historicalEventViewModel = HistoricalEventViewModel(historicalEventRequirement)
    }

    private fun initializeRecyclerView() {
        historicalEventAdapter = HistoricalEventAdapter(emptyList())
        mainActivityBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainActivityBinding.recyclerView.adapter = historicalEventAdapter
    }

    private fun initializeObservers() {
        historicalEventViewModel.events.observe(this) { events ->
            if (events != null) {
                historicalEventAdapter.updateData(events)
            }
        }

        historicalEventViewModel.error.observe(this) { errorMessage ->
            Log.d("ErrorActivity", errorMessage.toString())
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSearch() {
        mainActivityBinding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().toLowerCase()

                val filteredList = historicalEventViewModel.events.value?.filter { event ->
                    event.date?.toLowerCase()?.contains(query) == true ||
                            event.description?.toLowerCase()?.contains(query) == true ||
                            event.date?.toLowerCase()?.contains(query) == true ||
                            event.category1?.toLowerCase()?.contains(query) == true ||
                            event.category2?.toLowerCase()?.contains(query) == true
                }

                if (query.isEmpty()) {
                    historicalEventAdapter.updateData(historicalEventViewModel.events.value ?: emptyList())
                } else {
                    historicalEventAdapter.updateData(filteredList ?: emptyList())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
