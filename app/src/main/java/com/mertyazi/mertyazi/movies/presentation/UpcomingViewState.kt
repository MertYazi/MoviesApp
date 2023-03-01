package com.mertyazi.mertyazi.movies.presentation

data class UpcomingViewState(
    var results: MutableList<ResultViewState>,
    val totalPages: Int
)