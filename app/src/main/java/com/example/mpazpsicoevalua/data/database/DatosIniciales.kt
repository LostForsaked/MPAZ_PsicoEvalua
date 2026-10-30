package com.example.mpazpsicoevalua.data.database

import com.example.mpazpsicoevalua.data.entity.EstadoInstrumento
import com.example.mpazpsicoevalua.data.entity.Estudiante
import com.example.mpazpsicoevalua.data.entity.Instrumento
import com.example.mpazpsicoevalua.data.entity.Pregunta
import com.example.mpazpsicoevalua.data.entity.TipoInstrumento
import com.example.mpazpsicoevalua.data.entity.Usuario
import com.example.mpazpsicoevalua.util.Seguridad

object DatosIniciales {

    suspend fun cargar(db: AppDatabase) {
        // Si ya hay datos, no se vuelve a cargar
        if (db.usuarioDao().contar() > 0) return

        // Psicólogo ficticio
        db.usuarioDao().insertar(
            Usuario(
                nombreUsuario = "psicologo",
                passwordHash = Seguridad.hashear("Psico2026!"),
                nombreCompleto = "Psicólogo PIE (Demo)"
            )
        )

        // Estudiantes ficticios
        db.estudianteDao().insertarTodos(
            listOf(
                Estudiante(identificador = "EST-001", curso = "4° Básico A"),
                Estudiante(identificador = "EST-002", curso = "6° Básico B"),
                Estudiante(identificador = "EST-003", curso = "8° Básico A")
            )
        )

        // Instrumento 1: cuestionario educativo propio
        val idHabitos = db.instrumentoDao().insertar(
            Instrumento(
                nombre = "Cuestionario de Hábitos de Estudio (Demo)",
                tipo = TipoInstrumento.CUESTIONARIO_EDUCATIVO,
                descripcion = "Cuestionario educativo de práctica creado para probar la aplicación.",
                rangoEdad = "9 a 14 años",
                instrucciones = "Lee cada afirmación y elige la alternativa que mejor te describa. No hay respuestas correctas ni incorrectas.",
                tiempoEstimadoMin = 10,
                escala = "Nunca|A veces|Casi siempre|Siempre",
                permitePuntaje = true,
                estado = EstadoInstrumento.DISPONIBLE
            )
        ).toInt()

        agregarPreguntas(
            db, idHabitos, listOf(
                "Reviso mis cuadernos después de clases.",
                "Hago mis tareas antes de jugar o ver televisión.",
                "Me preparo con anticipación para las pruebas.",
                "Pido ayuda cuando no entiendo un contenido.",
                "Tengo un lugar tranquilo para estudiar en casa."
            )
        )

        // Instrumento 2: prueba simulada
        val idClima = db.instrumentoDao().insertar(
            Instrumento(
                nombre = "Escala Simulada de Clima Escolar (Demo)",
                tipo = TipoInstrumento.PRUEBA_SIMULADA,
                descripcion = "Instrumento simulado con ítems ficticios, solo para demostrar el funcionamiento de una prueba con puntaje.",
                rangoEdad = "10 a 14 años",
                instrucciones = "Indica qué tan de acuerdo estás con cada frase. Responde con sinceridad.",
                tiempoEstimadoMin = 8,
                escala = "Nada de acuerdo|Poco de acuerdo|Bastante de acuerdo|Totalmente de acuerdo",
                permitePuntaje = true,
                estado = EstadoInstrumento.DISPONIBLE
            )
        ).toInt()

        agregarPreguntas(
            db, idClima, listOf(
                "Me siento a gusto en mi sala de clases.",
                "Mis compañeros me tratan con respeto.",
                "Me gusta participar en las actividades del curso.",
                "Siento que mis profesores me escuchan.",
                "Vengo contento(a) al colegio."
            )
        )

        // Instrumento 3: pendiente de autorización (sin preguntas ni puntaje)
        db.instrumentoDao().insertar(
            Instrumento(
                nombre = "Prueba Estandarizada (Pendiente de autorización)",
                tipo = TipoInstrumento.PRUEBA_SIMULADA,
                descripcion = "Marcador de posición: solo podría incorporarse con autorización institucional y licencia.",
                rangoEdad = "No definido",
                instrucciones = "No disponible.",
                tiempoEstimadoMin = 0,
                escala = "",
                permitePuntaje = false,
                estado = EstadoInstrumento.PENDIENTE_AUTORIZACION
            )
        )
    }

    private suspend fun agregarPreguntas(db: AppDatabase, instrumentoId: Int, textos: List<String>) {
        db.preguntaDao().insertarTodas(
            textos.mapIndexed { indice, texto ->
                Pregunta(instrumentoId = instrumentoId, orden = indice + 1, texto = texto)
            }
        )
    }
}
