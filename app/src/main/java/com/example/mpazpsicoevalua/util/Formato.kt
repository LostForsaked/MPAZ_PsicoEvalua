package com.example.mpazpsicoevalua.util

import com.example.mpazpsicoevalua.data.entity.EstadoAplicacion
import com.example.mpazpsicoevalua.data.entity.EstadoInstrumento
import com.example.mpazpsicoevalua.data.entity.Instrumento
import com.example.mpazpsicoevalua.data.entity.TipoInstrumento
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatearFecha(millis: Long?): String {
    if (millis == null) return "—"
    return SimpleDateFormat("dd-MM-yyyy HH:mm", Locale("es", "CL")).format(Date(millis))
}

fun EstadoAplicacion.etiqueta(): String = when (this) {
    EstadoAplicacion.PENDIENTE -> "Pendiente"
    EstadoAplicacion.EN_DESARROLLO -> "En desarrollo"
    EstadoAplicacion.FINALIZADA -> "Finalizada"
    EstadoAplicacion.REVISADA -> "Revisada"
}

fun EstadoInstrumento.etiqueta(): String = when (this) {
    EstadoInstrumento.DISPONIBLE -> "Disponible"
    EstadoInstrumento.NO_DISPONIBLE -> "No disponible"
    EstadoInstrumento.PENDIENTE_AUTORIZACION -> "Pendiente de autorización"
}

fun TipoInstrumento.etiqueta(): String = when (this) {
    TipoInstrumento.CUESTIONARIO_EDUCATIVO -> "Cuestionario educativo"
    TipoInstrumento.PRUEBA_SIMULADA -> "Prueba simulada"
}

fun Instrumento.opciones(): List<String> = escala.split("|").filter { it.isNotBlank() }
