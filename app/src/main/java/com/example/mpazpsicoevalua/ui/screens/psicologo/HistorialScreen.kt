package com.example.mpazpsicoevalua.ui.screens.psicologo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.ui.components.Titulo
import com.example.mpazpsicoevalua.util.formatearFecha

@Composable
fun HistorialScreen(vm: MainViewModel, onVolver: () -> Unit) {
    val registros by vm.auditoria.collectAsState(initial = emptyList())

    PantallaBase {
        Titulo("Historial de acciones")

        if (registros.isEmpty()) {
            Text("Sin registros todavía.")
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(registros, key = { it.id }) { r ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(r.accion.replace("_", " "), fontWeight = FontWeight.Bold)
                        Text(r.detalle)
                        Text(formatearFecha(r.fecha), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        TextButton(onClick = onVolver) { Text("Volver") }
    }
}
