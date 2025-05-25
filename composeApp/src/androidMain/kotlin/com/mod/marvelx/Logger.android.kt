package com.mod.marvelx

import android.util.Log

actual fun appLog(level: LogLevel, message: String) {
    when (level) {
        LogLevel.VERBOSE -> Log.v("MANUAL", message)
        LogLevel.DEBUG -> Log.d("MANUAL", message)
        LogLevel.INFO -> Log.i("MANUAL", message)
        LogLevel.WARN -> Log.w("MANUAL", message)
        LogLevel.ERROR -> Log.e("MANUAL", message)
    }
}