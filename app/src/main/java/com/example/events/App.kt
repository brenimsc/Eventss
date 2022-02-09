package com.example.events

import android.os.Build
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.example.events.data.di.dataSource
import com.example.events.data.di.moduleNetwork
import com.example.events.data.di.repoModule
import com.example.events.data.di.viewModels
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
//import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : MultiDexApplication() {

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