package com.example.parcialtp3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Punto de entrada de Room. Lista las entidades (tablas) y expone los DAOs.
 * Room genera la implementacion (AppDatabase_Impl) en tiempo de compilacion con KSP.
 */
@Database(
    entities = [FavoriteQuote::class],
    version = 1,          // sube cuando cambia el esquema (agregar columnas, etc.)
    exportSchema = false, // no exporta el JSON del esquema; en el parcial no hace falta
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
