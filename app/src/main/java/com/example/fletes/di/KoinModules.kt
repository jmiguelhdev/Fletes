package com.example.fletes.di

import androidx.room.Room
import com.example.fletes.data.repositories.implementations.DestinationRepositoryImpl
import com.example.fletes.data.repositories.implementations.TruckRepositoryImpl
import com.example.fletes.data.repositories.implementations.TrucksJourneyRepositoryImp
import com.example.fletes.data.room.AppDatabase
import com.example.fletes.domain.validators.DniValidator
import com.example.fletes.domain.validators.PatenteValidator
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
    single<TruckRepositoryImpl> {
        TruckRepositoryImpl(get())
    }
    single<DestinationRepositoryImpl> {
        DestinationRepositoryImpl(get())
    }
    single<TrucksJourneyRepositoryImp> {
        TrucksJourneyRepositoryImp(get())
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