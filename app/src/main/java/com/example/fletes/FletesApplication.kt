package com.example.fletes


import android.app.Application
import com.example.fletes.di.appDatabaseModule
import com.example.fletes.di.buyDataModule
import com.example.fletes.di.camionModule
import com.example.fletes.di.dispatchModule
import com.example.fletes.di.domainModule
import com.example.fletes.di.journeyModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FletesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidLogger() // Optional: To see Koin logs
            androidContext(this@FletesApplication) // Provide the application context
            modules(
                appDatabaseModule,
                camionModule,
                domainModule,
                dispatchModule,
                journeyModule,
                buyDataModule

            ) // Your module with database
            // ... other modules
        }
    }
}