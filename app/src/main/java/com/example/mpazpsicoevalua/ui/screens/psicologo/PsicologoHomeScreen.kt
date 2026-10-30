package com.example.mpazpsicoevalua.ui.screens.psicologo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.mpazpsicoevalua.util.etiqueta
import com.example.mpazpsicoevalua.util.formatearFecha

@Composable
fun PsicologoHomeScreen(
    vm: MainViewModel,
    onCatalogo: () -> Unit,
    onHistorial: () -> Unit,
    onDetalle: (Int) -> Unit,
    onSalir: () -> Unit
) {
    val psicologo by vm.psicologo.collectAsState()
    val resumenes by vm.resumenes.collectAsState(initial = emptyList())

    PantallaBase {
        Titulo("Panel del profesional")
        Text(psicologo?.nombreCompleto ?: "")
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onCatalogo, modifier = Modifier.weight(1f)) { Text("Nueva aplicación") }
            OutlinedButton(onClick = onHistorial, modifier = Modifier.weight(1f)) { Text("Historial") }
        }

        Spacer(Modifier.height(16.dp))
        Text("Aplicaciones", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (resumenes.isEmpty()) {
            Text("Aún no hay aplicaciones. Crea una desde \"Nueva aplicación\".")
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(resumenes, key = { it.id }) { r ->
                Card(modifier = Modifier.fillMaxWidth().clickable { onDetalle(r.id) }) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(r.instrumentoNombre, fontWeight = FontWeight.Bold)
                        Text("${r.estudianteIdentificador} · ${r.curso}")
                        Text("Estado: ${r.estado.etiqueta()}")
                        Text("Creada: ${formatearFecha(r.fechaCreacion)}")
                    }
                }
            }
        }

        TextButton(onClick = onSalir) { Text("Cerrar sesión") }
    }
}
