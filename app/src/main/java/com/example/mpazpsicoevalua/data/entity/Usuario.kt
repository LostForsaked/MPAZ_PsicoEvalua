package com.example.mpazpsicoevalua.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["nombreUsuario"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreUsuario: String,
    val passwordHash: String,
    val nombreCompleto: String
)
