package com.mertyazi.mertyazi.favorites.business.mapper

import com.mertyazi.mertyazi.favorites.data.repository.database.FavoriteMovieEntity
import com.mertyazi.mertyazi.favorites.presentation.FavoriteMovieViewState
import javax.inject.Inject

class RandomToFavoriteViewStateMapper @Inject constructor(
): Function1<FavoriteMovieEntity, FavoriteMovieViewState> {

    override fun invoke(favoriteMovie: FavoriteMovieEntity): FavoriteMovieViewState {
        return FavoriteMovieViewState(
            favoriteMovie.id,
            favoriteMovie.movieTitle,
            favoriteMovie.moviePoster,
            favoriteMovie.moviePosterWithTitle
        )
    }
}