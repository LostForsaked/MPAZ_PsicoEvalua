package com.example.mpazpsicoevalua.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instrumentos")
data class Instrumento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipo: TipoInstrumento,
    val descripcion: String,
    val rangoEdad: String,
    val instrucciones: String,
    val tiempoEstimadoMin: Int,
    val escala: String,                    // "Nunca|A veces|Casi siempre|Siempre"
    val permitePuntaje: Boolean,
    val estado: EstadoInstrumento
)
