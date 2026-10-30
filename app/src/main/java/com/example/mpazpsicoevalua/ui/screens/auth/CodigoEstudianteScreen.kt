package com.example.mpazpsicoevalua.ui.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.ui.components.Titulo
import kotlinx.coroutines.launch

@Composable
fun CodigoEstudianteScreen(vm: MainViewModel, onIngreso: (Int) -> Unit, onVolver: () -> Unit) {
    var codigo by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    PantallaBase {
        Titulo("Ingreso de estudiante")
        Text("Escribe el código de 6 dígitos que te entregó el profesional.")
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = codigo,
            onValueChange = {
                if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                    codigo = it
                    error = null
                }
            },
            label = { Text("Código de acceso") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (codigo.length != 6) {
                    error = "El código tiene 6 dígitos."
                } else {
                    scope.launch {
                        val id = vm.ingresarConCodigo(codigo)
                        if (id != null) onIngreso(id)
                        else error = "Código inválido, vencido o ya utilizado."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) { Text("Entrar") }

        TextButton(onClick = onVolver) { Text("Volver") }
    }
}
