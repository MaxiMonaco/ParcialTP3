package com.example.parcialtp3.di

import android.content.Context
import androidx.room.Room
import com.example.parcialtp3.data.local.AppDatabase
import com.example.parcialtp3.data.local.FavoriteDao
import com.example.parcialtp3.data.local.MealDao
import com.example.parcialtp3.data.local.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Le ensena a Hilt como construir la base de datos y el DAO.
 * Despues cualquier clase puede pedirlos con @Inject, sin crearlos a mano.
 */
@Module
@InstallIn(SingletonComponent::class) // vive mientras viva la app
object DatabaseModule {

    // @Singleton: una sola instancia de la base para toda la app.
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "quotes.db")
            // SOLO DESARROLLO: al subir la version del esquema, Room recrea la base en vez de
            // cerrar la app. Antes de una entrega con datos reales hay que escribir migraciones.
            .fallbackToDestructiveMigration()
            .build()

    // El DAO sale de la base; no hace falta @Singleton porque la base ya lo es.
    @Provides
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun provideWorkoutDao(database: AppDatabase): WorkoutDao = database.workoutDao()

    @Provides
    fun provideMealDao(database: AppDatabase): MealDao = database.mealDao()
}
