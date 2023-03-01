package com.mertyazi.mertyazi.movies.presentation

data class NowPlayingViewState(
    var results: MutableList<ResultViewState>,
    val totalPages: Int
)