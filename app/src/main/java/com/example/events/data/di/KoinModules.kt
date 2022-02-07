package com.example.events.data.di

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.events.data.database.EventsDatabase
import com.example.events.data.repository.DetailsRepository
import com.example.events.data.repository.EventsRepository
import com.example.events.data.service.EventsApiService
import com.example.events.ui.viewmodel.DetailsActivityViewModel
import com.example.events.ui.viewmodel.EventsViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://5f5a8f24d44d640016169133.mockapi.io/api/"

val moduleNetwork = module {
    single {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    single {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    single {
        createService<EventsApiService>(get(), get())
    }

}

val dataSource = module {
    single { EventsDatabase.getInstance(androidContext()) }
    single { EventsDatabase.getInstance(androidContext()).eventsDao }
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
}

val viewModels = module {
    viewModel { EventsViewModel(get()) }
    viewModel { DetailsActivityViewModel(get()) }
}

val repoModule = module {
    single { EventsRepository(get(), get()) }
    single { DetailsRepository(get(), get(), get()) }
}

private inline fun <reified T> createService(
    client: OkHttpClient,
    factory: Moshi,
): T {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(factory))
        .build()
        .create(T::class.java)
}