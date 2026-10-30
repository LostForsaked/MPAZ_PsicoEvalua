package com.example.mpazpsicoevalua.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auditoria")
data class RegistroAuditoria(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val psicologoId: Int,
    val accion: String,          // ej: "CREAR_APLICACION", "CONSULTAR_APLICACION"
    val detalle: String,
    val aplicacionId: Int? = null,
    val fecha: Long
)
