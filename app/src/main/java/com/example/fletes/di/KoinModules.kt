package com.example.fletes.di

import androidx.room.Room
import com.example.fletes.data.repositories.implementations.DestinationRepositoryImpl
import com.example.fletes.data.repositories.implementations.TruckRepositoryImpl
import com.example.fletes.data.repositories.interfaces.DestinationRepositoryInterface
import com.example.fletes.data.room.AppDatabase
import com.example.fletes.domain.DeleteDestinoUseCase
import com.example.fletes.domain.GetAllDestinosUseCase
import com.example.fletes.domain.InsertDestinoUseCase
import com.example.fletes.domain.SearchComisionistaUseCase
import com.example.fletes.domain.SearchLocalidadUseCase
import com.example.fletes.domain.validators.DniValidator
import com.example.fletes.domain.validators.PatenteValidator
import com.example.fletes.ui.camion.CamionViewModel
import com.example.fletes.ui.destino.DispatchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appDatabaseModule = module {
    single {
        Room.databaseBuilder(
                androidContext(), // Use the context provided by Koin
                AppDatabase::class.java,
                "fletes_database" // Name of your database
            ).fallbackToDestructiveMigration(false)
            .build()
    }
    // Provide AppDao
    single {
        get<AppDatabase>().truckDao()
    }
    single {
        get<AppDatabase>().destinationDao()
    }
    single {
        get<AppDatabase>().trucksRegistrationDao()
    }

    // Provide Repositories
    single<TruckRepositoryImpl> {
        TruckRepositoryImpl(get())
    }
    single<DestinationRepositoryInterface> {
        DestinationRepositoryImpl(get())
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
    single { InsertDestinoUseCase(get()) }
    single { GetAllDestinosUseCase(get()) }
    single { SearchComisionistaUseCase(get()) }
    single { SearchLocalidadUseCase(get()) }
    single { DeleteDestinoUseCase(get()) }

}

val dispatchModule = module {
    viewModel {
        DispatchViewModel(
            destinationRepository = get(),
            searchComisionistaUseCase = get(),
            searchLocalidadUseCase = get(),
            getAllDestinosUseCase = get(),
            insertDestinoUseCase = get(),
            deleteDestinoUseCase = get()
        )
    }
}