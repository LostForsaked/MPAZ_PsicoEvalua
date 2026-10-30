package com.example.mpazpsicoevalua.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "estudiantes",
    indices = [Index(value = ["identificador"], unique = true)]
)
data class Estudiante(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val identificador: String,   // ej: "EST-001" (ficticio)
    val curso: String            // ej: "5° Básico A" (ficticio)
)
