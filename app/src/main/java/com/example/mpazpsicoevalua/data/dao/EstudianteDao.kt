package com.example.mpazpsicoevalua.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mpazpsicoevalua.data.entity.Estudiante
import kotlinx.coroutines.flow.Flow

@Dao
interface EstudianteDao {

    @Insert
    suspend fun insertarTodos(estudiantes: List<Estudiante>)

    @Query("SELECT * FROM estudiantes ORDER BY identificador")
    fun obtenerTodos(): Flow<List<Estudiante>>

    @Query("SELECT * FROM estudiantes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Estudiante?
}
