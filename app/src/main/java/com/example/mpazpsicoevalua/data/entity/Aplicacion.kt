package com.example.mpazpsicoevalua.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "aplicaciones",
    foreignKeys = [
        ForeignKey(
            entity = Instrumento::class,
            parentColumns = ["id"],
            childColumns = ["instrumentoId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Estudiante::class,
            parentColumns = ["id"],
            childColumns = ["estudianteId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["psicologoId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("instrumentoId"),
        Index("estudianteId"),
        Index("psicologoId"),
        Index(value = ["codigoAcceso"], unique = true)
    ]
)
data class Aplicacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val instrumentoId: Int,
    val estudianteId: Int,
    val psicologoId: Int,
    val codigoAcceso: String,
    val codigoExpiraEn: Long,
    val codigoUsado: Boolean = false,
    val estado: EstadoAplicacion = EstadoAplicacion.PENDIENTE,
    val fechaCreacion: Long,
    val fechaInicio: Long? = null,
    val fechaFin: Long? = null,
    val puntajePrueba: Int? = null,
    val observacion: String? = null
)
