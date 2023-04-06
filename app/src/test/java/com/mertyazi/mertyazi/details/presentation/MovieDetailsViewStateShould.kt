package com.mertyazi.mertyazi.details.presentation

import junit.framework.TestCase.assertTrue
import org.junit.Test

class MovieDetailsViewStateShould {

    @Test
    fun updateDetailsViewState() {
        val details = MovieDetailsViewState.ContentMovieDetails(
            DetailsViewState(
                "backdrop_path",
                listOf(),
                "homepage",
                1,
                "imdb_id",
                "overview",
                "poster_path",
                "release_date",
                90,
                "status",
                "title",
                "0.0",
                false
            )
        )
        val result = details.updateFavoriteMovie(
            DetailsViewState(
                "backdrop_path",
                listOf(),
                "homepage",
                1,
                "imdb_id",
                "overview",
                "poster_path",
                "release_date",
                90,
                "status",
                "title",
                "0.0",
                false
            ),
            true
        )
        assertTrue(result.details.isFavorite)
    }
}