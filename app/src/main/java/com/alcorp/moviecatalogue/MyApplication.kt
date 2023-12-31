package com.alcorp.moviecatalogue

import android.app.Application
import com.alcorp.moviecatalogue.core.di.databaseModule
import com.alcorp.moviecatalogue.core.di.networkModule
import com.alcorp.moviecatalogue.core.di.repositoryModule
import com.alcorp.moviecatalogue.di.useCaseModule
import com.alcorp.moviecatalogue.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}
