package com.utils

import android.app.Application
import com.example.kotlin.examen.data.network.NetworkModuleDI

class Examen : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkModuleDI.initializeParse(this)
    }
}