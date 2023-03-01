package com.mertyazi.mertyazi.movies.presentation

data class ResultViewState(
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: String
)