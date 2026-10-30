package com.example.mpazpsicoevalua.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "respuestas",
    foreignKeys = [
        ForeignKey(
            entity = Aplicacion::class,
            parentColumns = ["id"],
            childColumns = ["aplicacionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Pregunta::class,
            parentColumns = ["id"],
            childColumns = ["preguntaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["aplicacionId", "preguntaId"], unique = true),
        Index("preguntaId")
    ]
)
data class Respuesta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val aplicacionId: Int,
    val preguntaId: Int,
    val valor: Int               // posición elegida en la escala (1, 2, 3...)
)
