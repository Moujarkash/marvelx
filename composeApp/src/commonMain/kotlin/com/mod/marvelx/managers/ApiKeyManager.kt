package com.mod.marvelx.managers

import com.mod.marvelx.crypto.MD5
import com.mod.marvelx.storage.SecureKeyStorage

class ApiKeyManager(private val secureStorage: SecureKeyStorage) {

    suspend fun storeApiKeys(apiKey: String, privateKey: String) {
        secureStorage.storeKey("marvel_api_key", apiKey)
        secureStorage.storeKey("marvel_private_key", privateKey)
    }

    suspend fun getApiKey(): String? {
        return secureStorage.getKey("marvel_api_key")
    }

    private suspend fun getPrivateKey(): String? {
        return secureStorage.getKey("marvel_private_key")
    }

    suspend fun generateHash(timestamp: String): String? {
        val apiKey = getApiKey()
        val privateKey = getPrivateKey()

        if (apiKey != null && privateKey != null) {
            val input = timestamp + privateKey + apiKey
            return MD5.hash(input)
        }
        return null
    }
}