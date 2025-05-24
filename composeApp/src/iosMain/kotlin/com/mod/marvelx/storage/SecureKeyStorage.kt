package com.mod.marvelx.storage

import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Security.*

actual class SecureKeyStorage {

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun storeKey(key: String, value: String): Boolean {
        val keyData = key.encodeToByteArray()
        val valueData = value.encodeToByteArray()

        val query = CFDictionaryCreateMutable(null, 0, null, null)

        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, CFStringCreateWithCString(null, key, kCFStringEncodingUTF8))
        valueData.usePinned { pinned ->
            val nsData = CFDataCreate(null, pinned.addressOf(0).reinterpret(), valueData.size.convert())
            CFDictionaryAddValue(query, kSecValueData, nsData)
        }
        CFDictionaryAddValue(query, kSecAttrAccessible, kSecAttrAccessibleWhenUnlockedThisDeviceOnly)

        // Delete existing item first
        SecItemDelete(query)

        val status = SecItemAdd(query, null)
        CFRelease(query)

        return status == errSecSuccess
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun getKey(key: String): String? {
        val query = CFDictionaryCreateMutable(null, 0, null, null)

        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, CFStringCreateWithCString(null, key, kCFStringEncodingUTF8))
        CFDictionaryAddValue(query, kSecReturnData, kCFBooleanTrue)
        CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)

        memScoped {
            val result = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(query, result.ptr)
            CFRelease(query)

            return if (status == errSecSuccess && result.value != null) {
                val data = result.value as CFDataRef
                val length = CFDataGetLength(data).toInt()
                val bytes = CFDataGetBytePtr(data)
                bytes?.readBytes(length)?.decodeToString()
            } else {
                null
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun deleteKey(key: String): Boolean {
        val query = CFDictionaryCreateMutable(null, 0, null, null)

        CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
        CFDictionaryAddValue(query, kSecAttrAccount, CFStringCreateWithCString(null, key, kCFStringEncodingUTF8))

        val status = SecItemDelete(query)
        CFRelease(query)

        return status == errSecSuccess || status == errSecItemNotFound
    }
}