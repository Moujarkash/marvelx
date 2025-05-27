package com.mod.marvelx

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import com.mod.marvelx.database.AppDatabase
import com.mod.marvelx.di.appModule
import com.mod.marvelx.storage.SecureKeyStorage
import com.mod.marvelx.ui.screens.MainScreen
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
        MainScreen()
    }
}