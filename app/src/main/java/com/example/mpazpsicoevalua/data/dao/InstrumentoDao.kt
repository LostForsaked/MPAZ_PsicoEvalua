package com.example.mpazpsicoevalua.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mpazpsicoevalua.data.entity.Instrumento
import kotlinx.coroutines.flow.Flow

@Dao
interface InstrumentoDao {

    @Insert
    suspend fun insertar(instrumento: Instrumento): Long

    @Query("SELECT * FROM instrumentos ORDER BY nombre")
    fun obtenerTodos(): Flow<List<Instrumento>>

    @Query("SELECT * FROM instrumentos WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Instrumento?
}
