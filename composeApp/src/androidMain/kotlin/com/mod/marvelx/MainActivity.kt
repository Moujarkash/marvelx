package com.mod.marvelx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.mod.marvelx.database.getDatabaseBuilder
import com.mod.marvelx.storage.SecureKeyStorage
import com.mod.marvelx.storage.createDataStoreAndroid

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(
                prefs = remember {
                    createDataStoreAndroid(applicationContext)
                },
                secureKeyStorage = remember {
                    SecureKeyStorage(applicationContext)
                },
                databaseBuilder = remember {
                    getDatabaseBuilder(applicationContext)
                }
            )
        }
    }
}