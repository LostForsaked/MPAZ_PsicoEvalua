package com.example.mpazpsicoevalua.data.entity

data class AplicacionResumen(
    val id: Int,
    val estado: EstadoAplicacion,
    val fechaCreacion: Long,
    val fechaFin: Long?,
    val instrumentoNombre: String,
    val estudianteIdentificador: String,
    val curso: String
)
