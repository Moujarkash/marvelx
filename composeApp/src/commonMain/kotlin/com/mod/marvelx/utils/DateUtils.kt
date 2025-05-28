package com.mod.marvelx.utils

fun formatDate(dateString: String): String {
    return try {
        dateString.split("T")[0] 
    } catch (e: Exception) {
        dateString
    }
}