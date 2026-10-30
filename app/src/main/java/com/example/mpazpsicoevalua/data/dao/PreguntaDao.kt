package com.example.mpazpsicoevalua.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mpazpsicoevalua.data.entity.Pregunta

@Dao
interface PreguntaDao {

    @Insert
    suspend fun insertarTodas(preguntas: List<Pregunta>)

    @Query("SELECT * FROM preguntas WHERE instrumentoId = :instrumentoId ORDER BY orden")
    suspend fun obtenerPorInstrumento(instrumentoId: Int): List<Pregunta>
}
