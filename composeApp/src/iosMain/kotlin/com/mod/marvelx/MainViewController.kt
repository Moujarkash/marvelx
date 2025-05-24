package com.mod.marvelx

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.mod.marvelx.database.getDatabaseBuilder
import com.mod.marvelx.storage.SecureKeyStorage
import com.mod.marvelx.storage.createDataStore

fun MainViewController() = ComposeUIViewController {
    App(
        prefs = remember {
            createDataStore()
        },
        secureKeyStorage = remember {
            SecureKeyStorage()
        },
        databaseBuilder = remember {
            getDatabaseBuilder()
        }
    )
}