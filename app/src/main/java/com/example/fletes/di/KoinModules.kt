package com.example.fletes.di

import CamionRepository
import androidx.room.Room
import com.example.fletes.data.repositories.CamionRepositoryImpl
import com.example.fletes.data.room.AppDatabase
import com.example.fletes.domain.DniValidator
import com.example.fletes.domain.PatenteValidator
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
        get<AppDatabase>().truckDao()
        get<AppDatabase>().destinationDao()
        get<AppDatabase>().trucksRegistrationDao()
    }

    // Provide Repositories
    single<CamionRepository> {
        CamionRepositoryImpl(get())
    }
}

val camionModule = module {
    viewModel {
        CamionViewModel(
            camionRepository = get(),
            dniValidator = get(),
            licenseStringValidatorResult = get()
        )
    }
}

val domainModule = module {
    single { DniValidator() } // Provide DniValidator as a singleton
    single { PatenteValidator() }
}