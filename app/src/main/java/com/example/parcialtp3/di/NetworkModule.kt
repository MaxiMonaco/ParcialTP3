package com.example.parcialtp3.di

import com.example.parcialtp3.BuildConfig
import com.example.parcialtp3.data.remote.QuotesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Tiene que terminar en "/" o Retrofit tira un error al construirse.
    private const val BASE_URL = "https://api.api-ninjas.com/"

    // OkHttp es el cliente HTTP real; Retrofit trabaja encima.
    // El interceptor agrega el header X-Api-Key a TODAS las llamadas.
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-Api-Key", BuildConfig.API_NINJAS_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()

    // GsonConverterFactory convierte el JSON a los data class (QuoteDto).
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // Retrofit genera la implementacion de la interfaz QuotesApi.
    @Provides
    @Singleton
    fun provideQuotesApi(retrofit: Retrofit): QuotesApi =
        retrofit.create(QuotesApi::class.java)
}
