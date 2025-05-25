package com.mod.marvelx

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.mod.marvelx.database.getDatabaseBuilder
import com.mod.marvelx.storage.SecureKeyStorage
import com.mod.marvelx.storage.createDataStoreIos

fun MainViewController() = ComposeUIViewController {
    App(
        prefs = remember {
            createDataStoreIos()
        },
        secureKeyStorage = remember {
            SecureKeyStorage()
        },
        databaseBuilder = remember {
            getDatabaseBuilder()
        }
    )
}