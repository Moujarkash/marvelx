package com.mod.marvelx.crypto

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_MD5

@OptIn(ExperimentalForeignApi::class)
actual object MD5 {
    actual fun hash(input: String): String {
        val inputData = input.encodeToByteArray()
        val digest = UByteArray(16) // MD5 produces 16-byte hash

        inputData.usePinned { pinned ->
            CC_MD5(pinned.addressOf(0), inputData.size.toUInt(), digest.refTo(0))
        }

        return digest.joinToString("") { byte ->
            val hex = byte.toInt().toString(16)
            if (hex.length == 1) "0$hex" else hex
        }
    }
}