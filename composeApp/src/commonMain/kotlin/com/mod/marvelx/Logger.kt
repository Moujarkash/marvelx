package com.mod.marvelx

enum class LogLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
}

expect fun appLog(level: LogLevel, message: String)