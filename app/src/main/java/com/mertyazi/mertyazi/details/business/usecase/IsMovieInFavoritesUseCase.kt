package com.mertyazi.mertyazi.details.business.usecase

import com.mertyazi.mertyazi.favorites.business.repository.FavoritesRepository
import javax.inject.Inject

class IsMovieInFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend fun execute(movieId: String): Boolean =
        favoritesRepository.isFavorite(movieId)
}