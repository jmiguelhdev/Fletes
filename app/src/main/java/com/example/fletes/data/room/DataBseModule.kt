package com.example.fletes.data.room

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appDatabaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(), // Use the context provided by Koin
            AppDatabase::class.java,
            "fletes_database" // Name of your database
        ).build()
    }
    // Provide AppDao
    single {
        get<AppDatabase>().appDao()
    }
}