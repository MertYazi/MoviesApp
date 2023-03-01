package com.mertyazi.mertyazi.favorites.business.repository

import com.mertyazi.mertyazi.favorites.data.repository.database.FavoriteMovieEntity
import com.mertyazi.mertyazi.shared.data.repository.api.Result

interface FavoritesRepository {

    suspend fun isFavorite(movieId : String) : Boolean

    suspend fun getFavorites(): Result<List<FavoriteMovieEntity>>

    suspend fun getRandom(): Result<FavoriteMovieEntity?>

    suspend fun addToFavorites(movie : FavoriteMovieEntity)

    suspend fun removeFromFavorites(movie : FavoriteMovieEntity)
}