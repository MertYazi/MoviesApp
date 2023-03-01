package com.mertyazi.mertyazi.details.business.mapper

import com.mertyazi.mertyazi.details.business.entities.Genre
import com.mertyazi.mertyazi.details.business.entities.MovieDetails
import com.mertyazi.mertyazi.shared.presentation.DateFormatter
import com.mertyazi.mertyazi.details.presentation.DetailsViewState
import com.mertyazi.mertyazi.shared.presentation.ImdbScoreFormatter
import javax.inject.Inject

class DetailsToDetailsViewStateMapper @Inject constructor(
    private val imdbScoreFormatter: ImdbScoreFormatter,
    private val dateFormatter: DateFormatter
): Function1<MovieDetails, DetailsViewState> {

    override fun invoke(movieDetails: MovieDetails): DetailsViewState {
        return DetailsViewState(
            movieDetails.backdrop_path ?: "",
            movieDetails.genres?.map {
                Genre(
                    it.id,
                    it.name
                )
            },
            movieDetails.homepage ?: "",
            movieDetails.id,
            movieDetails.imdb_id ?: "",
            movieDetails.overview ?: "",
            movieDetails.poster_path ?: "",
            dateFormatter.format(movieDetails.release_date),
            movieDetails.runtime ?: 0,
            movieDetails.status ?: "",
            movieDetails.title + dateFormatter.minify(movieDetails.release_date),
            imdbScoreFormatter.format(movieDetails.vote_average)
        )
    }
}