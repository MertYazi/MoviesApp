package com.mertyazi.mertyazi.favorites.data.repository

import com.mertyazi.mertyazi.favorites.business.repository.FavoritesRepository
import com.mertyazi.mertyazi.favorites.data.repository.database.FavoriteMovieEntity
import com.mertyazi.mertyazi.favorites.data.repository.database.FavoritesDAO
import com.mertyazi.mertyazi.shared.data.repository.api.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesDatabaseRepository @Inject constructor(
    private val databaseDAO: FavoritesDAO
) : FavoritesRepository {
    override suspend fun isFavorite(movieId: String): Boolean {
        return withContext(Dispatchers.IO) {
            databaseDAO.isMovieFavorite(movieId) != null
        }
    }

    override suspend fun getFavorites(): Result<List<FavoriteMovieEntity>> {
        return withContext(Dispatchers.IO) {
            Result.Success(databaseDAO.getAllFavoriteMovies())
        }
    }

    override suspend fun getRandom(): Result<FavoriteMovieEntity> {
        return withContext(Dispatchers.IO) {
            Result.Success(databaseDAO.getRandomMovie())
        }
    }

    override suspend fun addToFavorites(movie: FavoriteMovieEntity) {
        return withContext(Dispatchers.IO) {
            databaseDAO.addMovieToFavorites(
                movie
            )
        }
    }

    override suspend fun removeFromFavorites(movie: FavoriteMovieEntity) {
        return withContext(Dispatchers.IO) {
            databaseDAO.removeMovieFromFavorites(
                movie
            )
        }
    }
}