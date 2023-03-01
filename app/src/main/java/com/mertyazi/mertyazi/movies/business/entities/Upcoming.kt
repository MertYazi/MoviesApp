package com.mertyazi.mertyazi.movies.business.entities

data class Upcoming(
    var results: MutableList<Result>,
    val totalPages: Int
)