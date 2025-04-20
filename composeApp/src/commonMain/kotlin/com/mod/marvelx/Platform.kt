package com.mod.marvelx

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform