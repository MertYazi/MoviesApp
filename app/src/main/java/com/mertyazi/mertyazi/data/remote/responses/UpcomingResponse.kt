package com.mertyazi.mertyazi.data.remote.responses

data class UpcomingResponse(
    val dates: Dates,
    val page: Int,
    var results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)