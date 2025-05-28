package com.mod.marvelx.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

actual fun openUrl(url: String, context: Any?) {
    val androidContext = context as Context
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    androidContext.startActivity(intent)
}