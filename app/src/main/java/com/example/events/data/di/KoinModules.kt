package com.example.events.data.di


import android.content.SharedPreferences
import android.os.Build
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
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager


private const val BASE_URL = "https://5f5a8f24d44d640016169133.mockapi.io/"

val moduleNetwork = module {
    single {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    single {
        createService<EventsApiService>(get())
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
    factory: Moshi
): T {
    var builder = Retrofit.Builder()
        .baseUrl(BASE_URL)

    if (Build.VERSION.SDK_INT in 19..21) {
        builder = builder.client(createOkHttpClient())
    }

    return builder.addConverterFactory(MoshiConverterFactory.create(factory))
        .build()
        .create(T::class.java)
}

private fun createOkHttpClient(): OkHttpClient {
    return try {
        val trustAllCerts: Array<TrustManager> = arrayOf(MyManager())
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory)
            .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
            .build()
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}

