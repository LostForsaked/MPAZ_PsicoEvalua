package com.example.mpazpsicoevalua.ui.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.ui.components.Titulo

@Composable
fun InicioScreen(onPsicologo: () -> Unit, onEstudiante: () -> Unit) {
    PantallaBase {
        Spacer(Modifier.weight(1f))
        Titulo("MPAZ PsicoEvalúa")
        Text("Aplicación supervisada de instrumentos. Todos los datos son de demostración y ficticios.")
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onPsicologo,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) { Text("Soy psicólogo(a) PIE") }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = onEstudiante,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) { Text("Soy estudiante") }
        Spacer(Modifier.weight(1f))
    }
}
