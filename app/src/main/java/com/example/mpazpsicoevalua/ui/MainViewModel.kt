package com.example.mpazpsicoevalua.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mpazpsicoevalua.MpazApp
import com.example.mpazpsicoevalua.data.entity.Instrumento
import com.example.mpazpsicoevalua.data.entity.Usuario
import com.example.mpazpsicoevalua.data.repository.DetalleAplicacion
import com.example.mpazpsicoevalua.data.repository.PruebaCargada
import com.example.mpazpsicoevalua.data.repository.Repositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = Repositorio((application as MpazApp).database)

    private val _psicologo = MutableStateFlow<Usuario?>(null)
    val psicologo: StateFlow<Usuario?> = _psicologo.asStateFlow()

    val instrumentos = repo.instrumentos()
    val estudiantes = repo.estudiantes()
    val resumenes = repo.resumenes()
    val auditoria = repo.auditoria()

    // ----- Sesión del psicólogo -----

    suspend fun loginPsicologo(usuario: String, password: String): Boolean {
        val u = repo.loginPsicologo(usuario, password)
        _psicologo.value = u
        return u != null
    }

    fun cerrarSesion() {
        _psicologo.value = null
    }

    // ----- Psicólogo -----

    suspend fun instrumento(id: Int): Instrumento? = repo.instrumento(id)

    suspend fun crearAplicacion(instrumentoId: Int, estudianteId: Int): String? {
        val p = _psicologo.value ?: return null
        return repo.crearAplicacion(p.id, instrumentoId, estudianteId)
    }

    suspend fun detalle(aplicacionId: Int, registrar: Boolean): DetalleAplicacion? {
        val p = _psicologo.value ?: return null
        return repo.consultarDetalle(p.id, aplicacionId, registrar)
    }

    suspend fun guardarObservacion(aplicacionId: Int, observacion: String) {
        val p = _psicologo.value ?: return
        repo.guardarObservacion(p.id, aplicacionId, observacion)
    }

    // ----- Estudiante -----

    suspend fun ingresarConCodigo(codigo: String): Int? = repo.ingresarConCodigo(codigo)

    suspend fun cargarPrueba(aplicacionId: Int): PruebaCargada? = repo.cargarPrueba(aplicacionId)

    suspend fun finalizar(aplicacionId: Int, respuestas: Map<Int, Int>) =
        repo.finalizar(aplicacionId, respuestas)
}
