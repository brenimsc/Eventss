package com.example.events

import android.app.Application
import com.example.events.di.dataSource
import com.example.events.di.moduleNetwork
import com.example.events.di.repoModule
import com.example.events.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    moduleNetwork,
                    dataSource,
                    viewModels,
                    repoModule
                )
            )
        }
    }
}