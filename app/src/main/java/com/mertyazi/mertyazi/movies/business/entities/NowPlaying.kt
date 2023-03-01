package com.mertyazi.mertyazi.movies.business.entities

data class NowPlaying(
    var results: MutableList<Result>,
    val totalPages: Int
)