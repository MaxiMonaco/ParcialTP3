package com.example.parcialtp3.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Las 4 operaciones que pide el enunciado:
 * insertar, borrar por id, listar todas y saber si un id existe.
 */
@Dao
interface FavoriteDao {

    // suspend: corre en una coroutine, fuera del hilo principal.
    // REPLACE: si ya existe una fila con ese id, la reemplaza en lugar de fallar.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: FavoriteQuote)

    @Query("DELETE FROM favorite_quotes WHERE id = :id")
    suspend fun deleteById(id: String)

    // Flow: emite la lista de nuevo cada vez que la tabla cambia.
    // No lleva suspend porque devolver un Flow no bloquea nada.
    @Query("SELECT * FROM favorite_quotes")
    fun getAll(): Flow<List<FavoriteQuote>>

    // Devuelve true si hay al menos una fila con ese id. Tambien es Flow,
    // asi el boton "Guardar / Quitar favorito" se actualiza solo.
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_quotes WHERE id = :id)")
    fun exists(id: String): Flow<Boolean>
}
