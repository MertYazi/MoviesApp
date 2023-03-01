package com.mertyazi.mertyazi.details.presentation


sealed class MovieDetailsViewState {
    object Loading : MovieDetailsViewState()
    object Error : MovieDetailsViewState()
    data class ContentMovieDetails(val details: DetailsViewState) : MovieDetailsViewState()
}

fun MovieDetailsViewState.ContentMovieDetails.updateFavoriteMovie(
    movie: DetailsViewState,
    isFavorite: Boolean
): MovieDetailsViewState.ContentMovieDetails {
    return MovieDetailsViewState.ContentMovieDetails(details =
        if (movie.id == details.id) {
            movie.isFavorite = isFavorite
            movie
        } else {
            movie
        }
    )
}