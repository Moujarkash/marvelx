package com.mod.marvelx.crypto

expect object MD5 {
    fun hash(input: String): String
}