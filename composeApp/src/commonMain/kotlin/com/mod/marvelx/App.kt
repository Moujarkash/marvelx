package com.mod.marvelx

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import com.mod.marvelx.database.AppDatabase
import com.mod.marvelx.di.appModule
import com.mod.marvelx.storage.SecureKeyStorage
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@Composable
@Preview
fun App(
    prefs: DataStore<Preferences>,
    secureKeyStorage: SecureKeyStorage,
    databaseBuilder: RoomDatabase.Builder<AppDatabase>
) {
    KoinApplication(application = {
        modules(module {
            single<DataStore<Preferences>> { prefs }
            single<SecureKeyStorage> { secureKeyStorage }
        }, appModule(databaseBuilder))
    }) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                var text by remember { mutableStateOf("Loading") }
                val scope = rememberCoroutineScope()
                LaunchedEffect(true) {
                    scope.launch {
                        text = try {
                            Greeting().greeting()
                        } catch (e: Exception) {
                            appLog(LogLevel.ERROR,e.message ?: "error")
                            e.message ?: "error"
                        }
                    }
                }
                GreetingView(text)
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MaterialTheme {
        GreetingView("Hello, Android!")
    }
}