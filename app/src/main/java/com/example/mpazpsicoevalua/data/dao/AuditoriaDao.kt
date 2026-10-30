package com.example.mpazpsicoevalua.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mpazpsicoevalua.data.entity.RegistroAuditoria
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditoriaDao {

    @Insert
    suspend fun insertar(registro: RegistroAuditoria)

    @Query("SELECT * FROM auditoria ORDER BY fecha DESC")
    fun obtenerTodos(): Flow<List<RegistroAuditoria>>
}
