package com.example.mpazpsicoevalua.ui.screens.estudiante

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.ui.components.Titulo

@Composable
fun FinScreen(onSalir: () -> Unit) {
    BackHandler { }

    PantallaBase {
        Spacer(Modifier.weight(1f))
        Titulo("¡Listo!")
        Text("Tus respuestas fueron enviadas. Gracias por participar. Avisa al profesional que terminaste.")
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onSalir,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) { Text("Salir") }
        Spacer(Modifier.weight(1f))
    }
}
