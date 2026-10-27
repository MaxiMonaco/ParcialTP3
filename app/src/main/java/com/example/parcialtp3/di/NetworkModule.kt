package com.example.parcialtp3.di

import com.example.parcialtp3.data.remote.WorkoutsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Tiene que terminar en "/" o Retrofit tira un error al construirse.
    // VORA Mock API (consigna, diapositiva 8). Sin autenticacion. Con el servidor local en el
    // emulador seria "http://10.0.2.2:8000/" y habria que habilitar HTTP solo para desarrollo.
    private const val BASE_URL = "https://tp3.martinrivas.ar/"

    // OkHttp es el cliente HTTP real; Retrofit trabaja encima. La API es publica (sin X-Api-Key).
    // Timeouts de 15 s: el servidor de pruebas puede demorar hasta 10 s con X-Mock-Delay-Ms.
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

    // GsonConverterFactory convierte el JSON a los data class (WorkoutDto, ApiResponse).
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // Retrofit genera la implementacion de WorkoutsApi (catalogo y detalle de entrenamientos).
    @Provides
    @Singleton
    fun provideWorkoutsApi(retrofit: Retrofit): WorkoutsApi =
        retrofit.create(WorkoutsApi::class.java)
}
