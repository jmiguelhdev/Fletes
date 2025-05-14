package com.example.fletes.di

import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.example.fletes.data.repositories.implementations.DestinationRepositoryImpl
import com.example.fletes.data.repositories.implementations.TruckRepositoryImpl
import com.example.fletes.data.repositories.implementations.TrucksJourneyRepositoryImp
import com.example.fletes.data.repositories.interfaces.DestinationRepositoryInterface
import com.example.fletes.data.repositories.interfaces.TrucksJourneyRepositoryInterface
import com.example.fletes.data.room.AppDatabase
import com.example.fletes.domain.DeleteDestinoUseCase
import com.example.fletes.domain.GetActiveDestinosUseCase
import com.example.fletes.domain.GetActiveDispatchCount
import com.example.fletes.domain.GetAllDestinosUseCase
import com.example.fletes.domain.GetAllJourneyUseCase
import com.example.fletes.domain.GetUnActiveDispatchUseCase
import com.example.fletes.domain.InsertDestinoUseCase
import com.example.fletes.domain.InsertJourneyUseCase
import com.example.fletes.domain.SearchComisionistaUseCase
import com.example.fletes.domain.SearchLocalidadUseCase
import com.example.fletes.domain.UpdateDestinoUseCase
import com.example.fletes.domain.UpdateJourneyUseCase
import com.example.fletes.domain.validators.DniValidator
import com.example.fletes.domain.validators.PatenteValidator
import com.example.fletes.ui.screenTruck.TruckViewModel
import com.example.fletes.ui.screenDispatch.NewDispatchViewModel
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
    single<TrucksJourneyRepositoryInterface> {
        TrucksJourneyRepositoryImp(get())
    }
}

val camionModule = module {
    viewModel {
        TruckViewModel(
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
    single { InsertDestinoUseCase(get()) }
    single { DeleteDestinoUseCase(get()) }
    single { UpdateDestinoUseCase(get()) }
    single { GetActiveDestinosUseCase(get()) }
    single { GetActiveDispatchCount(get()) }
    single { GetUnActiveDispatchUseCase(get()) }
    single { InsertJourneyUseCase(get()) }
    single { UpdateJourneyUseCase(get()) }
    single { GetAllJourneyUseCase(get()) }
    single { InsertJourneyUseCase(get()) }

}

val dispatchModule = module {
    viewModel {parameters ->
        val savedStateHandle = parameters.get<SavedStateHandle>()
        NewDispatchViewModel(
            //savedStateHandle = savedStateHandle,
            getActiveDispatch = get(),
            getUnActiveDispatch = get(),
            getActiveDispatchCount = get(),
            searchComisionistaUseCase = get(),
            searchLocalidadUseCase = get(),
            getAllDestinosUseCase = get(),
            insertDestinoUseCase = get(),
            deleteDestinoUseCase = get(),
            updateDestinoUseCase = get(),
            createJourneyUseCase = get()
        )
    }
}