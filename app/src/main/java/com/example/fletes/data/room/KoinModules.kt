package com.example.fletes.di

import androidx.room.Room
import com.example.fletes.data.repositories.CamionRepository
import com.example.fletes.data.repositories.CamionRepositoryImpl
import com.example.fletes.data.room.AppDatabase
import com.example.fletes.ui.camion.CamionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appDatabaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(), // Use the context provided by Koin
            AppDatabase::class.java,
            "fletes_database" // Name of your database
        ).fallbackToDestructiveMigration()
            .build()
    }
    // Provide AppDao
    single {
        get<AppDatabase>().appDao()
    }

    // Provide Repositories
    single<CamionRepository> {
        CamionRepositoryImpl(get())
    }
}

val camionModule = module {
    viewModel { CamionViewModel(get()) }
}