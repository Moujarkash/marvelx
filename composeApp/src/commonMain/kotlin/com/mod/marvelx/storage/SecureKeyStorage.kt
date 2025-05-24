package com.mod.marvelx.storage

expect class SecureKeyStorage {
    suspend fun storeKey(key: String, value: String): Boolean
    suspend fun getKey(key: String): String?
    suspend fun deleteKey(key: String): Boolean
}