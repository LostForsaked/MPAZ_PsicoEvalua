package com.example.mpazpsicoevalua.ui.screens.estudiante

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.data.repository.PruebaCargada
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.util.opciones
import kotlinx.coroutines.launch

@Composable
fun PruebaScreen(vm: MainViewModel, aplicacionId: Int, onTerminar: () -> Unit) {
    var prueba by remember { mutableStateOf<PruebaCargada?>(null) }
    var indice by remember { mutableIntStateOf(0) }
    val respuestas = remember { mutableStateMapOf<Int, Int>() }
    var enviando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(aplicacionId) { prueba = vm.cargarPrueba(aplicacionId) }

    // El estudiante no puede salir de la prueba con el botón atrás
    BackHandler { }

    PantallaBase {
        val p = prueba
        if (p == null || p.preguntas.isEmpty()) {
            Text("Cargando…")
        } else {
            val pregunta = p.preguntas[indice]
            val opciones = p.instrumento.opciones()
            val esUltima = indice == p.preguntas.size - 1

            Text(
                "Pregunta ${indice + 1} de ${p.preguntas.size}",
                style = MaterialTheme.typography.titleMedium
            )
            LinearProgressIndicator(
                progress = (indice + 1f) / p.preguntas.size,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(pregunta.texto, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            opciones.forEachIndexed { pos, texto ->
                val valor = pos + 1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { respuestas[pregunta.id] = valor }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = respuestas[pregunta.id] == valor,
                        onClick = { respuestas[pregunta.id] = valor }
                    )
                    Text(texto, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { indice-- },
                    enabled = indice > 0 && !enviando,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) { Text("Anterior") }

                Button(
                    onClick = {
                        if (esUltima) {
                            enviando = true
                            scope.launch {
                                vm.finalizar(aplicacionId, respuestas.toMap())
                                onTerminar()
                            }
                        } else {
                            indice++
                        }
                    },
                    enabled = respuestas.containsKey(pregunta.id) && !enviando,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) { Text(if (esUltima) "Finalizar" else "Siguiente") }
            }
        }
    }
}
