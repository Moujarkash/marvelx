package com.mod.marvelx.crypto

import java.security.MessageDigest

actual object MD5 {
    actual fun hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}