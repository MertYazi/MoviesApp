package com.mertyazi.mertyazi.movies.data.entities

data class NowPlayingEntity(
    val dates: DatesEntity,
    val page: Int,
    var results: MutableList<ResultEntity>,
    val total_pages: Int,
    val total_results: Int
)