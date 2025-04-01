package com.example.fletes


import android.app.Application
import com.example.fletes.data.room.appDatabaseModule
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
            modules(appDatabaseModule) // Your module with database
            // ... other modules
        }
    }
}