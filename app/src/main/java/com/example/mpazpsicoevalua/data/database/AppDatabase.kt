package com.example.mpazpsicoevalua.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mpazpsicoevalua.data.dao.AplicacionDao
import com.example.mpazpsicoevalua.data.dao.AuditoriaDao
import com.example.mpazpsicoevalua.data.dao.EstudianteDao
import com.example.mpazpsicoevalua.data.dao.InstrumentoDao
import com.example.mpazpsicoevalua.data.dao.PreguntaDao
import com.example.mpazpsicoevalua.data.dao.RespuestaDao
import com.example.mpazpsicoevalua.data.dao.UsuarioDao
import com.example.mpazpsicoevalua.data.entity.Aplicacion
import com.example.mpazpsicoevalua.data.entity.Estudiante
import com.example.mpazpsicoevalua.data.entity.Instrumento
import com.example.mpazpsicoevalua.data.entity.Pregunta
import com.example.mpazpsicoevalua.data.entity.RegistroAuditoria
import com.example.mpazpsicoevalua.data.entity.Respuesta
import com.example.mpazpsicoevalua.data.entity.Usuario

@Database(
    entities = [
        Usuario::class,
        Estudiante::class,
        Instrumento::class,
        Pregunta::class,
        Aplicacion::class,
        Respuesta::class,
        RegistroAuditoria::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun estudianteDao(): EstudianteDao
    abstract fun instrumentoDao(): InstrumentoDao
    abstract fun preguntaDao(): PreguntaDao
    abstract fun aplicacionDao(): AplicacionDao
    abstract fun respuestaDao(): RespuestaDao
    abstract fun auditoriaDao(): AuditoriaDao

    companion object {
        @Volatile
        private var INSTANCIA: AppDatabase? = null

        fun obtener(context: Context): AppDatabase {
            return INSTANCIA ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mpaz_psicoevalua.db"
                ).build().also { INSTANCIA = it }
            }
        }
    }
}
