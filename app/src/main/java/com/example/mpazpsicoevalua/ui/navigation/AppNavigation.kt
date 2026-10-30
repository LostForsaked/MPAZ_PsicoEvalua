package com.example.mpazpsicoevalua.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mpazpsicoevalua.ui.MainViewModel
import com.example.mpazpsicoevalua.ui.screens.auth.CodigoEstudianteScreen
import com.example.mpazpsicoevalua.ui.screens.auth.InicioScreen
import com.example.mpazpsicoevalua.ui.screens.auth.LoginPsicologoScreen
import com.example.mpazpsicoevalua.ui.screens.estudiante.FinScreen
import com.example.mpazpsicoevalua.ui.screens.estudiante.InstruccionesScreen
import com.example.mpazpsicoevalua.ui.screens.estudiante.PruebaScreen
import com.example.mpazpsicoevalua.ui.screens.psicologo.CatalogoScreen
import com.example.mpazpsicoevalua.ui.screens.psicologo.DetalleAplicacionScreen
import com.example.mpazpsicoevalua.ui.screens.psicologo.HistorialScreen
import com.example.mpazpsicoevalua.ui.screens.psicologo.NuevaAplicacionScreen
import com.example.mpazpsicoevalua.ui.screens.psicologo.PsicologoHomeScreen

object Rutas {
    const val INICIO = "inicio"
    const val LOGIN = "login_psicologo"
    const val CODIGO = "codigo_estudiante"

    const val HOME = "psicologo/home"
    const val CATALOGO = "psicologo/catalogo"
    const val NUEVA = "psicologo/nueva/{instrumentoId}"
    const val DETALLE = "psicologo/detalle/{aplicacionId}"
    const val HISTORIAL = "psicologo/historial"

    const val INSTRUCCIONES = "estudiante/instrucciones/{aplicacionId}"
    const val PRUEBA = "estudiante/prueba/{aplicacionId}"
    const val FIN = "estudiante/fin"

    fun nueva(instrumentoId: Int) = "psicologo/nueva/$instrumentoId"
    fun detalle(aplicacionId: Int) = "psicologo/detalle/$aplicacionId"
    fun instrucciones(aplicacionId: Int) = "estudiante/instrucciones/$aplicacionId"
    fun prueba(aplicacionId: Int) = "estudiante/prueba/$aplicacionId"
}

private fun irAInicio(nav: NavHostController) {
    nav.navigate(Rutas.INICIO) {
        popUpTo(Rutas.INICIO) { inclusive = true }
    }
}

/** Solo muestra el contenido si hay un psicólogo con sesión iniciada; si no, vuelve al inicio. */
@Composable
private fun Protegido(vm: MainViewModel, nav: NavHostController, contenido: @Composable () -> Unit) {
    val psicologo by vm.psicologo.collectAsState()
    if (psicologo == null) {
        LaunchedEffect(Unit) { irAInicio(nav) }
    } else {
        contenido()
    }
}

@Composable
fun AppNavigation(vm: MainViewModel) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rutas.INICIO) {

        composable(Rutas.INICIO) {
            // Volver al inicio siempre cierra la sesión del profesional
            LaunchedEffect(Unit) { vm.cerrarSesion() }
            InicioScreen(
                onPsicologo = { nav.navigate(Rutas.LOGIN) },
                onEstudiante = { nav.navigate(Rutas.CODIGO) }
            )
        }

        composable(Rutas.LOGIN) {
            LoginPsicologoScreen(
                vm = vm,
                onExito = { nav.navigate(Rutas.HOME) { popUpTo(Rutas.INICIO) } },
                onVolver = { nav.popBackStack() }
            )
        }

        composable(Rutas.CODIGO) {
            CodigoEstudianteScreen(
                vm = vm,
                onIngreso = { id -> nav.navigate(Rutas.instrucciones(id)) { popUpTo(Rutas.INICIO) } },
                onVolver = { nav.popBackStack() }
            )
        }

        // ---------- Perfil psicólogo (protegido) ----------

        composable(Rutas.HOME) {
            Protegido(vm, nav) {
                PsicologoHomeScreen(
                    vm = vm,
                    onCatalogo = { nav.navigate(Rutas.CATALOGO) },
                    onHistorial = { nav.navigate(Rutas.HISTORIAL) },
                    onDetalle = { id -> nav.navigate(Rutas.detalle(id)) },
                    onSalir = { irAInicio(nav) }
                )
            }
        }

        composable(Rutas.CATALOGO) {
            Protegido(vm, nav) {
                CatalogoScreen(
                    vm = vm,
                    onElegir = { id -> nav.navigate(Rutas.nueva(id)) },
                    onVolver = { nav.popBackStack() }
                )
            }
        }

        composable(
            Rutas.NUEVA,
            arguments = listOf(navArgument("instrumentoId") { type = NavType.IntType })
        ) { entry ->
            val instrumentoId = entry.arguments?.getInt("instrumentoId") ?: 0
            Protegido(vm, nav) {
                NuevaAplicacionScreen(
                    vm = vm,
                    instrumentoId = instrumentoId,
                    onListo = { nav.popBackStack(Rutas.HOME, inclusive = false) },
                    onVolver = { nav.popBackStack() }
                )
            }
        }

        composable(
            Rutas.DETALLE,
            arguments = listOf(navArgument("aplicacionId") { type = NavType.IntType })
        ) { entry ->
            val aplicacionId = entry.arguments?.getInt("aplicacionId") ?: 0
            Protegido(vm, nav) {
                DetalleAplicacionScreen(
                    vm = vm,
                    aplicacionId = aplicacionId,
                    onVolver = { nav.popBackStack() }
                )
            }
        }

        composable(Rutas.HISTORIAL) {
            Protegido(vm, nav) {
                HistorialScreen(vm = vm, onVolver = { nav.popBackStack() })
            }
        }

        // ---------- Perfil estudiante ----------

        composable(
            Rutas.INSTRUCCIONES,
            arguments = listOf(navArgument("aplicacionId") { type = NavType.IntType })
        ) { entry ->
            val aplicacionId = entry.arguments?.getInt("aplicacionId") ?: 0
            InstruccionesScreen(
                vm = vm,
                aplicacionId = aplicacionId,
                onComenzar = { nav.navigate(Rutas.prueba(aplicacionId)) { popUpTo(Rutas.INICIO) } },
                onSalir = { irAInicio(nav) }
            )
        }

        composable(
            Rutas.PRUEBA,
            arguments = listOf(navArgument("aplicacionId") { type = NavType.IntType })
        ) { entry ->
            val aplicacionId = entry.arguments?.getInt("aplicacionId") ?: 0
            PruebaScreen(
                vm = vm,
                aplicacionId = aplicacionId,
                onTerminar = { nav.navigate(Rutas.FIN) { popUpTo(Rutas.INICIO) } }
            )
        }

        composable(Rutas.FIN) {
            FinScreen(onSalir = { irAInicio(nav) })
        }
    }
}
