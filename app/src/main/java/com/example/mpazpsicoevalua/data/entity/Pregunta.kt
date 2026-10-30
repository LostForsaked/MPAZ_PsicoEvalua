package com.example.mpazpsicoevalua.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "preguntas",
    foreignKeys = [
        ForeignKey(
            entity = Instrumento::class,
            parentColumns = ["id"],
            childColumns = ["instrumentoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("instrumentoId")]
)
data class Pregunta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val instrumentoId: Int,
    val orden: Int,
    val texto: String
)
