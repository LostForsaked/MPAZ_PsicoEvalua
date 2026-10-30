package com.example.mpazpsicoevalua.ui.screens.psicologo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.data.entity.Instrumento
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.ui.components.Titulo
import kotlinx.coroutines.launch

@Composable
fun NuevaAplicacionScreen(
    vm: MainViewModel,
    instrumentoId: Int,
    onListo: () -> Unit,
    onVolver: () -> Unit
) {
    var instrumento by remember { mutableStateOf<Instrumento?>(null) }
    var seleccionado by remember { mutableStateOf<Int?>(null) }
    var codigo by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val estudiantes by vm.estudiantes.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    LaunchedEffect(instrumentoId) { instrumento = vm.instrumento(instrumentoId) }

    PantallaBase {
        Titulo("Nueva aplicación")
        Text(instrumento?.nombre ?: "Cargando…", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        val codigoActual = codigo
        if (codigoActual == null) {
            Text("Selecciona el estudiante (ficticio):")
            Spacer(Modifier.height(8.dp))

            estudiantes.forEach { e ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { seleccionado = e.id; error = null },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = seleccionado == e.id,
                        onClick = { seleccionado = e.id; error = null }
                    )
                    Text("${e.identificador} · ${e.curso}")
                }
            }

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    val estudianteId = seleccionado
                    if (estudianteId == null) {
                        error = "Selecciona un estudiante."
                    } else {
                        scope.launch {
                            val nuevo = vm.crearAplicacion(instrumentoId, estudianteId)
                            if (nuevo == null) error = "No se pudo crear la aplicación."
                            else codigo = nuevo
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Generar código temporal") }

            TextButton(onClick = onVolver) { Text("Cancelar") }
        } else {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Código temporal de acceso")
                    Spacer(Modifier.height(8.dp))
                    Text(
                        codigoActual,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Válido por 30 minutos y de un solo uso. Entrégalo al estudiante.")
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onListo,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) { Text("Volver al panel") }
        }
    }
}
