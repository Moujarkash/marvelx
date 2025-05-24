package com.mod.marvelx.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.nio.charset.StandardCharsets
import android.util.Base64

actual class SecureKeyStorage(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_keys")

    private val aead: Aead by lazy {
        AeadConfig.register()

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "secure_keyset", "secure_keyset_pref")
            .withKeyTemplate(KeyTemplates.get("AES128_GCM"))
            .withMasterKeyUri("android-keystore://secure_master_key")
            .build()
            .keysetHandle

        keysetHandle.getPrimitive(Aead::class.java)
    }

    actual suspend fun storeKey(key: String, value: String): Boolean {
        return try {
            val encryptedValue = encrypt(value)
            val prefKey = stringPreferencesKey(key)
            context.dataStore.edit { preferences ->
                preferences[prefKey] = encryptedValue
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    actual suspend fun getKey(key: String): String? {
        return try {
            val prefKey = stringPreferencesKey(key)
            val encryptedValue = context.dataStore.data
                .map { preferences -> preferences[prefKey] }
                .first()

            encryptedValue?.let { decrypt(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    actual suspend fun deleteKey(key: String): Boolean {
        return try {
            val prefKey = stringPreferencesKey(key)
            context.dataStore.edit { preferences ->
                preferences.remove(prefKey)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun encrypt(plaintext: String): String {
        val ciphertext = aead.encrypt(
            plaintext.toByteArray(StandardCharsets.UTF_8),
            null // No associated data
        )
        return Base64.encodeToString(ciphertext, Base64.DEFAULT)
    }

    private fun decrypt(encryptedText: String): String {
        val ciphertext = Base64.decode(encryptedText, Base64.DEFAULT)
        val plaintext = aead.decrypt(ciphertext, null)
        return String(plaintext, StandardCharsets.UTF_8)
    }
}