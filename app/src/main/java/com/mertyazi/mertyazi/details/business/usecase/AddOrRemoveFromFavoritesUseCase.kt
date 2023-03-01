package com.mertyazi.mertyazi.details.business.usecase

import com.mertyazi.mertyazi.details.presentation.DetailsViewState
import com.mertyazi.mertyazi.favorites.business.repository.FavoritesRepository
import com.mertyazi.mertyazi.favorites.data.repository.database.FavoriteMovieEntity
import javax.inject.Inject

class AddOrRemoveFromFavoritesUseCase @Inject constructor(
    private val isMovieInFavoritesUseCase: IsMovieInFavoritesUseCase,
    private val favoritesRepository: FavoritesRepository
) {
    suspend fun execute(movie: DetailsViewState) {
        if(isMovieInFavoritesUseCase.execute(movie.id.toString())){
            favoritesRepository.removeFromFavorites(
                FavoriteMovieEntity(
                    movie.id.toString(),
                    movie.title ?: "",
                    movie.backdrop_path ?: "",
                    movie.poster_path ?: ""
                )
            )
        } else {
            favoritesRepository.addToFavorites(
                FavoriteMovieEntity(
                    movie.id.toString(),
                    movie.title ?: "",
                    movie.backdrop_path ?: "",
                    movie.poster_path ?: ""
                )
            )
        }
    }
}