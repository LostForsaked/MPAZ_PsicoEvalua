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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.components.PantallaBase
import com.example.mpazpsicoevalua.ui.components.Titulo
import kotlinx.coroutines.launch

@Composable
fun LoginPsicologoScreen(vm: MainViewModel, onExito: () -> Unit, onVolver: () -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    PantallaBase {
        Titulo("Acceso profesional")

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it; error = null },
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; error = null },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (usuario.isBlank() || password.isBlank()) {
                    error = "Completa usuario y contraseña."
                } else {
                    scope.launch {
                        if (vm.loginPsicologo(usuario, password)) onExito()
                        else error = "Usuario o contraseña incorrectos."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) { Text("Ingresar") }

        TextButton(onClick = onVolver) { Text("Volver") }

        Spacer(Modifier.height(24.dp))
        Text(
            "Cuenta de demostración: psicologo / Psico2026!",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
