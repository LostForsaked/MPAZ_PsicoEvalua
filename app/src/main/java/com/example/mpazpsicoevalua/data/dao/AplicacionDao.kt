package com.example.mpazpsicoevalua.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mpazpsicoevalua.data.entity.Aplicacion
import com.example.mpazpsicoevalua.data.entity.AplicacionResumen
import kotlinx.coroutines.flow.Flow

@Dao
interface AplicacionDao {

    @Insert
    suspend fun insertar(aplicacion: Aplicacion): Long

    @Update
    suspend fun actualizar(aplicacion: Aplicacion)

    @Query("SELECT * FROM aplicaciones WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Aplicacion?

    @Query("SELECT * FROM aplicaciones WHERE codigoAcceso = :codigo LIMIT 1")
    suspend fun buscarPorCodigo(codigo: String): Aplicacion?

    @Query(
        """
        SELECT a.id AS id, a.estado AS estado, a.fechaCreacion AS fechaCreacion,
               a.fechaFin AS fechaFin, i.nombre AS instrumentoNombre,
               e.identificador AS estudianteIdentificador, e.curso AS curso
        FROM aplicaciones a
        INNER JOIN instrumentos i ON i.id = a.instrumentoId
        INNER JOIN estudiantes e ON e.id = a.estudianteId
        ORDER BY a.fechaCreacion DESC
        """
    )
    fun obtenerResumenes(): Flow<List<AplicacionResumen>>
}
