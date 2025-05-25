package com.mod.marvelx

actual fun appLog(level: LogLevel, message: String) {
    platform.Foundation.NSLog("Manual Log: $message")
}