package com.example.parcialtp3.data.repository

import com.example.parcialtp3.data.local.FavoriteDao
import com.example.parcialtp3.model.Quote
import com.example.parcialtp3.model.toEntity
import com.example.parcialtp3.model.toQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Unico lugar que habla con Room para los favoritos.
 * Las pantallas y ViewModels nunca tocan el DAO directamente.
 */
class FavoritesRepository @Inject constructor(
    private val dao: FavoriteDao,
) {

    // Lista de favoritos; se vuelve a emitir sola cada vez que la tabla cambia.
    val favorites: Flow<List<Quote>> =
        dao.getAll().map { list -> list.map { it.toQuote() } }

    // true/false segun si esa frase ya esta guardada (tambien reactivo).
    fun isFavorite(id: String): Flow<Boolean> = dao.exists(id)

    suspend fun add(quote: Quote) = dao.insert(quote.toEntity())

    suspend fun remove(id: String) = dao.deleteById(id)
}
