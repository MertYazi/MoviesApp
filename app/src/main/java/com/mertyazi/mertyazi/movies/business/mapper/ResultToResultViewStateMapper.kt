package com.mertyazi.mertyazi.movies.business.mapper

import com.mertyazi.mertyazi.shared.presentation.DateFormatter
import com.mertyazi.mertyazi.shared.presentation.ImdbScoreFormatter
import com.mertyazi.mertyazi.movies.business.entities.Result
import com.mertyazi.mertyazi.movies.presentation.ResultViewState
import com.mertyazi.mertyazi.shared.Constants.POSTER_PATH
import javax.inject.Inject

class ResultToResultViewStateMapper @Inject constructor(
    private val imdbScoreFormatter: ImdbScoreFormatter,
    private val dateFormatter: DateFormatter
): Function1<MutableList<Result>, MutableList<ResultViewState>> {

    override fun invoke(results: MutableList<Result>): MutableList<ResultViewState> {
        return results.map {
            ResultViewState(
                POSTER_PATH + it.backdrop_path,
                it.genre_ids,
                it.id,
                it.overview,
                it.poster_path,
                dateFormatter.format(it.release_date),
                it.title + dateFormatter.minify(it.release_date),
                imdbScoreFormatter.format(it.vote_average)
            )
        }.toMutableList()
    }
}