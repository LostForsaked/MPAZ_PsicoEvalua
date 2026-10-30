package com.example.mpazpsicoevalua.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpazpsicoevalua.data.entity.Respuesta
import com.example.mpazpsicoevalua.data.entity.RespuestaDetalle

@Dao
interface RespuestaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(respuesta: Respuesta)

    @Query("SELECT * FROM respuestas WHERE aplicacionId = :aplicacionId")
    suspend fun obtenerPorAplicacion(aplicacionId: Int): List<Respuesta>

    @Query(
        """
        SELECT p.orden AS orden, p.texto AS texto, r.valor AS valor
        FROM respuestas r
        INNER JOIN preguntas p ON p.id = r.preguntaId
        WHERE r.aplicacionId = :aplicacionId
        ORDER BY p.orden
        """
    )
    suspend fun obtenerDetalle(aplicacionId: Int): List<RespuestaDetalle>
}
