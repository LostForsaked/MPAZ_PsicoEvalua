package com.example.mpazpsicoevalua

import android.app.Application
import com.example.mpazpsicoevalua.data.database.AppDatabase
import com.example.mpazpsicoevalua.data.database.DatosIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MpazApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.obtener(this) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.cargar(database)
        }
    }
}
