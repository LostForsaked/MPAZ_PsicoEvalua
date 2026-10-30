package com.example.mpazpsicoevalua.ui.screens.psicologo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.data.entity.EstadoAplicacion
import com.example.mpazpsicoevalua.data.repository.DetalleAplicacion
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.ui.components.Titulo
import com.example.mpazpsicoevalua.util.etiqueta
import com.example.mpazpsicoevalua.util.formatearFecha
import com.example.mpazpsicoevalua.util.opciones
import kotlinx.coroutines.launch

@Composable
fun DetalleAplicacionScreen(vm: MainViewModel, aplicacionId: Int, onVolver: () -> Unit) {
    var detalle by remember { mutableStateOf<DetalleAplicacion?>(null) }
    var observacion by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(aplicacionId) {
        val d = vm.detalle(aplicacionId, registrar = true)
        detalle = d
        observacion = d?.aplicacion?.observacion ?: ""
    }

    PantallaBase {
        Titulo("Detalle de la aplicación")

        val d = detalle
        if (d == null) {
            Text("Cargando…", modifier = Modifier.weight(1f))
        } else {
            val a = d.aplicacion
            val finalizada = a.estado == EstadoAplicacion.FINALIZADA || a.estado == EstadoAplicacion.REVISADA

            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                Text(d.instrumento.nombre, fontWeight = FontWeight.Bold)
                Text("Estudiante: ${d.estudiante.identificador} · ${d.estudiante.curso}")
                Text("Estado: ${a.estado.etiqueta()}")
                Text("Creada: ${formatearFecha(a.fechaCreacion)}")
                Text("Inicio: ${formatearFecha(a.fechaInicio)}")
                Text("Fin: ${formatearFecha(a.fechaFin)}")
                if (a.estado == EstadoAplicacion.PENDIENTE) {
                    Text("Código: ${a.codigoAcceso} (vence ${formatearFecha(a.codigoExpiraEn)})")
                }
                Spacer(Modifier.height(16.dp))

                if (finalizada) {
                    Text("Respuestas", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    val opciones = d.instrumento.opciones()
                    d.respuestas.forEach { r ->
                        Text("${r.orden}. ${r.texto}")
                        Text(
                            "→ ${opciones.getOrElse(r.valor - 1) { r.valor.toString() }}",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    if (a.puntajePrueba != null) {
                        Text("Puntaje de prueba: ${a.puntajePrueba}", fontWeight = FontWeight.Bold)
                        Text(
                            "Valor de demostración. No constituye interpretación ni diagnóstico; la interpretación es responsabilidad exclusiva del profesional.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = observacion,
                        onValueChange = { observacion = it; mensaje = null },
                        label = { Text("Observación profesional (ficticia)") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        scope.launch {
                            vm.guardarObservacion(aplicacionId, observacion)
                            detalle = vm.detalle(aplicacionId, registrar = false)
                            mensaje = "Observación guardada. Aplicación marcada como revisada."
                        }
                    }) { Text("Guardar y marcar como revisada") }
                    mensaje?.let { Text(it) }
                } else {
                    Text("Las respuestas estarán disponibles cuando el estudiante finalice la aplicación.")
                }
            }
        }

        TextButton(onClick = onVolver) { Text("Volver") }
    }
}
