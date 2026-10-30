package com.example.mpazpsicoevalua.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mpazpsicoevalua.data.entity.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE nombreUsuario = :nombreUsuario LIMIT 1")
    suspend fun buscarPorNombreUsuario(nombreUsuario: String): Usuario?

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}
