package com.example.mpazpsicoevalua.ui.screens.psicologo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.data.entity.EstadoInstrumento
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.ui.components.Titulo
import com.example.mpazpsicoevalua.util.etiqueta

@Composable
fun CatalogoScreen(vm: MainViewModel, onElegir: (Int) -> Unit, onVolver: () -> Unit) {
    val instrumentos by vm.instrumentos.collectAsState(initial = emptyList())

    PantallaBase {
        Titulo("Catálogo de instrumentos")

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(instrumentos, key = { it.id }) { i ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(i.nombre, fontWeight = FontWeight.Bold)
                        Text(i.tipo.etiqueta())
                        Text("Edad: ${i.rangoEdad} · ${i.tiempoEstimadoMin} min")
                        Spacer(Modifier.height(4.dp))
                        Text(i.descripcion)
                        Spacer(Modifier.height(4.dp))
                        Text("Estado: ${i.estado.etiqueta()}")
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { onElegir(i.id) },
                            enabled = i.estado == EstadoInstrumento.DISPONIBLE
                        ) { Text("Aplicar") }
                    }
                }
            }
        }

        TextButton(onClick = onVolver) { Text("Volver") }
    }
}
