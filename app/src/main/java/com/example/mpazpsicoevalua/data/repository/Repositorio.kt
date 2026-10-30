package com.example.mpazpsicoevalua.data.repository

import com.example.mpazpsicoevalua.data.database.AppDatabase
import com.example.mpazpsicoevalua.data.entity.Aplicacion
import com.example.mpazpsicoevalua.data.entity.AplicacionResumen
import com.example.mpazpsicoevalua.data.entity.EstadoAplicacion
import com.example.mpazpsicoevalua.data.entity.EstadoInstrumento
import com.example.mpazpsicoevalua.data.entity.Estudiante
import com.example.mpazpsicoevalua.data.entity.Instrumento
import com.example.mpazpsicoevalua.data.entity.Pregunta
import com.example.mpazpsicoevalua.data.entity.RegistroAuditoria
import com.example.mpazpsicoevalua.data.entity.Respuesta
import com.example.mpazpsicoevalua.data.entity.RespuestaDetalle
import com.example.mpazpsicoevalua.data.entity.Usuario
import com.example.mpazpsicoevalua.util.Seguridad
import kotlinx.coroutines.flow.Flow

data class DetalleAplicacion(
    val aplicacion: Aplicacion,
    val instrumento: Instrumento,
    val estudiante: Estudiante,
    val respuestas: List<RespuestaDetalle>
)

data class PruebaCargada(
    val aplicacion: Aplicacion,
    val instrumento: Instrumento,
    val preguntas: List<Pregunta>
)

class Repositorio(private val db: AppDatabase) {

    companion object {
        const val VIGENCIA_CODIGO_MS = 30L * 60L * 1000L // 30 minutos
    }

    fun instrumentos(): Flow<List<Instrumento>> = db.instrumentoDao().obtenerTodos()
    fun estudiantes(): Flow<List<Estudiante>> = db.estudianteDao().obtenerTodos()
    fun resumenes(): Flow<List<AplicacionResumen>> = db.aplicacionDao().obtenerResumenes()
    fun auditoria(): Flow<List<RegistroAuditoria>> = db.auditoriaDao().obtenerTodos()

    suspend fun instrumento(id: Int): Instrumento? = db.instrumentoDao().buscarPorId(id)

    // ---------- Perfil psicólogo ----------

    suspend fun loginPsicologo(nombreUsuario: String, password: String): Usuario? {
        val usuario = db.usuarioDao().buscarPorNombreUsuario(nombreUsuario.trim()) ?: return null
        if (usuario.passwordHash != Seguridad.hashear(password)) return null
        auditar(usuario.id, "INICIAR_SESION", "Inicio de sesión del profesional")
        return usuario
    }

    suspend fun crearAplicacion(psicologoId: Int, instrumentoId: Int, estudianteId: Int): String? {
        val instrumento = db.instrumentoDao().buscarPorId(instrumentoId) ?: return null
        if (instrumento.estado != EstadoInstrumento.DISPONIBLE) return null

        var codigo = Seguridad.generarCodigo()
        while (db.aplicacionDao().buscarPorCodigo(codigo) != null) {
            codigo = Seguridad.generarCodigo()
        }

        val ahora = System.currentTimeMillis()
        val id = db.aplicacionDao().insertar(
            Aplicacion(
                instrumentoId = instrumentoId,
                estudianteId = estudianteId,
                psicologoId = psicologoId,
                codigoAcceso = codigo,
                codigoExpiraEn = ahora + VIGENCIA_CODIGO_MS,
                fechaCreacion = ahora
            )
        ).toInt()

        auditar(psicologoId, "CREAR_APLICACION", "Aplicación #$id creada (${instrumento.nombre})", id)
        return codigo
    }

    suspend fun consultarDetalle(psicologoId: Int, aplicacionId: Int, registrar: Boolean): DetalleAplicacion? {
        val a = db.aplicacionDao().buscarPorId(aplicacionId) ?: return null
        val i = db.instrumentoDao().buscarPorId(a.instrumentoId) ?: return null
        val e = db.estudianteDao().buscarPorId(a.estudianteId) ?: return null

        val puedeVerRespuestas = a.estado == EstadoAplicacion.FINALIZADA || a.estado == EstadoAplicacion.REVISADA
        val respuestas = if (puedeVerRespuestas) db.respuestaDao().obtenerDetalle(aplicacionId) else emptyList()

        if (registrar) {
            auditar(psicologoId, "CONSULTAR_APLICACION", "Consulta de la aplicación #$aplicacionId", aplicacionId)
        }
        return DetalleAplicacion(a, i, e, respuestas)
    }

    suspend fun guardarObservacion(psicologoId: Int, aplicacionId: Int, observacion: String) {
        val a = db.aplicacionDao().buscarPorId(aplicacionId) ?: return
        val nuevoEstado =
            if (a.estado == EstadoAplicacion.FINALIZADA || a.estado == EstadoAplicacion.REVISADA)
                EstadoAplicacion.REVISADA
            else a.estado

        db.aplicacionDao().actualizar(
            a.copy(observacion = observacion.trim().ifBlank { null }, estado = nuevoEstado)
        )
        auditar(psicologoId, "GUARDAR_OBSERVACION", "Observación guardada en la aplicación #$aplicacionId", aplicacionId)
    }

    // ---------- Perfil estudiante ----------

    /** Devuelve el id de la aplicación si el código es válido (vigente y no usado). */
    suspend fun ingresarConCodigo(codigo: String): Int? {
        val a = db.aplicacionDao().buscarPorCodigo(codigo.trim()) ?: return null
        val ahora = System.currentTimeMillis()
        if (a.codigoUsado || a.estado != EstadoAplicacion.PENDIENTE || ahora > a.codigoExpiraEn) return null

        db.aplicacionDao().actualizar(
            a.copy(codigoUsado = true, estado = EstadoAplicacion.EN_DESARROLLO, fechaInicio = ahora)
        )
        return a.id
    }

    suspend fun cargarPrueba(aplicacionId: Int): PruebaCargada? {
        val a = db.aplicacionDao().buscarPorId(aplicacionId) ?: return null
        if (a.estado != EstadoAplicacion.EN_DESARROLLO) return null
        val i = db.instrumentoDao().buscarPorId(a.instrumentoId) ?: return null
        val preguntas = db.preguntaDao().obtenerPorInstrumento(i.id)
        return PruebaCargada(a, i, preguntas)
    }

    /** Guarda las respuestas y calcula el puntaje de prueba (solo si el instrumento lo permite). No devuelve nada al estudiante. */
    suspend fun finalizar(aplicacionId: Int, respuestas: Map<Int, Int>) {
        val a = db.aplicacionDao().buscarPorId(aplicacionId) ?: return
        if (a.estado != EstadoAplicacion.EN_DESARROLLO) return
        val i = db.instrumentoDao().buscarPorId(a.instrumentoId) ?: return

        respuestas.forEach { (preguntaId, valor) ->
            db.respuestaDao().insertar(
                Respuesta(aplicacionId = aplicacionId, preguntaId = preguntaId, valor = valor)
            )
        }
        val puntaje = if (i.permitePuntaje) respuestas.values.sum() else null
        db.aplicacionDao().actualizar(
            a.copy(
                estado = EstadoAplicacion.FINALIZADA,
                fechaFin = System.currentTimeMillis(),
                puntajePrueba = puntaje
            )
        )
    }

    private suspend fun auditar(psicologoId: Int, accion: String, detalle: String, aplicacionId: Int? = null) {
        db.auditoriaDao().insertar(
            RegistroAuditoria(
                psicologoId = psicologoId,
                accion = accion,
                detalle = detalle,
                aplicacionId = aplicacionId,
                fecha = System.currentTimeMillis()
            )
        )
    }
}
