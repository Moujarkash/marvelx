package com.mod.marvelx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mod.marvelx.database.getDatabaseBuilder
import com.mod.marvelx.storage.SecureKeyStorage
import com.mod.marvelx.storage.createDataStoreAndroid

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { false }

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