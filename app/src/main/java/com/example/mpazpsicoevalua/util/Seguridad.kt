package com.example.mpazpsicoevalua.util

import java.security.MessageDigest
import java.security.SecureRandom

object Seguridad {

    private const val SAL = "mpaz-psicoevalua-demo"

    fun hashear(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest((SAL + password).toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun generarCodigo(): String {
        val numero = SecureRandom().nextInt(1_000_000)
        return "%06d".format(numero)
    }
}
