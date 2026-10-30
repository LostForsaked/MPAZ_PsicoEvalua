package com.example.mpazpsicoevalua

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.navigation.AppNavigation
// Si esta línea sale en rojo, usa el nombre del tema que está en ui/theme/Theme.kt
import com.example.mpazpsicoevalua.ui.theme.MPAZPsicoEvaluaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MPAZPsicoEvaluaTheme {
                val vm: MainViewModel = viewModel()
                AppNavigation(vm)
            }
        }
    }
}
