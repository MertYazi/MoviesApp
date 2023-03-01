package com.mertyazi.mertyazi.details.presentation

import com.mertyazi.mertyazi.details.business.entities.Genre

data class DetailsViewState(
    val backdrop_path: String?,
    val genres: List<Genre>?,
    val homepage: String?,
    val id: Int?,
    val imdb_id: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val runtime: Int?,
    val status: String?,
    val title: String?,
    val vote_average: String?,
    var isFavorite: Boolean = false
)