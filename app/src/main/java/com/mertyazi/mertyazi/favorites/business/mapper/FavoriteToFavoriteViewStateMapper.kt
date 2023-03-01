package com.mertyazi.mertyazi.favorites.business.mapper

import com.mertyazi.mertyazi.favorites.data.repository.database.FavoriteMovieEntity
import com.mertyazi.mertyazi.favorites.presentation.FavoriteMovieViewState
import javax.inject.Inject

class FavoriteToFavoriteViewStateMapper @Inject constructor(
): Function1<List<FavoriteMovieEntity>, List<FavoriteMovieViewState>> {

    override fun invoke(favoriteMovies: List<FavoriteMovieEntity>): List<FavoriteMovieViewState> {
        return favoriteMovies.map {
            FavoriteMovieViewState(
                it.id,
                it.movieTitle,
                it.moviePoster,
                it.moviePosterWithTitle
            )
        }
    }
}