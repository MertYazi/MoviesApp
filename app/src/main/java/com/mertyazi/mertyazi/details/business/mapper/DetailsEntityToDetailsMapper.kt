package com.mertyazi.mertyazi.details.business.mapper

import com.mertyazi.mertyazi.details.business.entities.Genre
import com.mertyazi.mertyazi.details.business.entities.MovieDetails
import com.mertyazi.mertyazi.details.data.entities.MovieDetailsEntity
import javax.inject.Inject

class DetailsEntityToDetailsMapper @Inject constructor(
): Function1<MovieDetailsEntity, MovieDetails> {

    override fun invoke(movieDetailsEntity: MovieDetailsEntity): MovieDetails {
        return MovieDetails(
            movieDetailsEntity.backdrop_path ?: "",
            movieDetailsEntity.genres?.map {
                Genre(
                    it.id,
                    it.name
                )
            },
            movieDetailsEntity.homepage ?: "",
            movieDetailsEntity.id,
            movieDetailsEntity.imdb_id ?: "",
            movieDetailsEntity.overview ?: "",
            movieDetailsEntity.poster_path ?: "",
            movieDetailsEntity.release_date ?: "",
            movieDetailsEntity.runtime ?: 0,
            movieDetailsEntity.status ?: "",
            movieDetailsEntity.title ?: "",
            movieDetailsEntity.vote_average ?: 0.0,
            movieDetailsEntity.isFavorite
        )
    }
}