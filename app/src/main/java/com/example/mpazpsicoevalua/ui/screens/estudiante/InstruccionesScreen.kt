package com.example.mpazpsicoevalua.ui.screens.estudiante

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.data.repository.PruebaCargada
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.ui.components.Titulo

@Composable
fun InstruccionesScreen(
    vm: MainViewModel,
    aplicacionId: Int,
    onComenzar: () -> Unit,
    onSalir: () -> Unit
) {
    var prueba by remember { mutableStateOf<PruebaCargada?>(null) }
    var cargado by remember { mutableStateOf(false) }

    LaunchedEffect(aplicacionId) {
        prueba = vm.cargarPrueba(aplicacionId)
        cargado = true
    }

    // El estudiante no puede volver atrás desde aquí
    BackHandler { }

    PantallaBase {
        val p = prueba
        if (p == null) {
            Text(if (cargado) "No se pudo cargar la aplicación. Avisa al profesional." else "Cargando…")
            if (cargado) {
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onSalir) { Text("Salir") }
            }
        } else {
            Titulo(p.instrumento.nombre)
            Text("Instrucciones", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(p.instrumento.instrucciones, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Text("Tiempo estimado: ${p.instrumento.tiempoEstimadoMin} minutos")
            Text("Cantidad de preguntas: ${p.preguntas.size}")
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onComenzar,
                enabled = p.preguntas.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) { Text("Comenzar") }
        }
    }
}
