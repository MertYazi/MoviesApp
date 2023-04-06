package com.mertyazi.mertyazi.details.business.usecase

import com.mertyazi.mertyazi.details.presentation.DetailsViewState
import com.mertyazi.mertyazi.favorites.business.mapper.DetailsViewStateToFavoriteMapper
import com.mertyazi.mertyazi.favorites.business.repository.FavoritesRepository
import javax.inject.Inject

class AddOrRemoveFromFavoritesUseCase @Inject constructor(
    private val isMovieInFavoritesUseCase: IsMovieInFavoritesUseCase,
    private val detailsViewStateToFavoriteMapper: DetailsViewStateToFavoriteMapper,
    private val favoritesRepository: FavoritesRepository
) {
    suspend fun execute(movie: DetailsViewState) {
        if(isMovieInFavoritesUseCase.execute(movie.id.toString())){
            favoritesRepository.removeFromFavorites(
                detailsViewStateToFavoriteMapper.invoke(movie)
            )
        } else {
            favoritesRepository.addToFavorites(
                detailsViewStateToFavoriteMapper.invoke(movie)
            )
        }
    }
}