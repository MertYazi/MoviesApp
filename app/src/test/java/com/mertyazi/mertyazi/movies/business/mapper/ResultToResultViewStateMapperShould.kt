package com.mertyazi.mertyazi.movies.business.mapper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import com.mertyazi.mertyazi.movies.business.entities.Result
import com.mertyazi.mertyazi.movies.presentation.ResultViewState
import com.mertyazi.mertyazi.shared.Constants.POSTER_PATH
import com.mertyazi.mertyazi.shared.presentation.DateFormatter
import com.mertyazi.mertyazi.shared.presentation.ImdbScoreFormatter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import org.junit.Before

@ExperimentalCoroutinesApi
class ResultToResultViewStateMapperShould {

    private val imdbScoreFormatter = mock<ImdbScoreFormatter>()
    private val dateFormatter = mock<DateFormatter>()

    private lateinit var result: Result

    private lateinit var mapper: ResultToResultViewStateMapper

    private lateinit var resultViewStates: MutableList<ResultViewState>

    private lateinit var resultViewState: ResultViewState

    @Before
    fun setup() {
        result = Result(
            "backdrop-path",
            listOf(1,3,5),
            1,
            "overview",
            "poster-path",
            "2022-12-12",
            "title",
            9.165
        )
        whenever(imdbScoreFormatter.format(result.vote_average)).thenReturn(
            "9.16"
        )
        whenever(dateFormatter.minify(result.release_date)).thenReturn(
            "(2022)"
        )
        whenever(dateFormatter.format(result.release_date)).thenReturn(
            "2022.12.12"
        )
        mapper = ResultToResultViewStateMapper(imdbScoreFormatter, dateFormatter)
        resultViewStates = mapper.invoke(mutableListOf(result))
        resultViewState = resultViewStates[0]
    }

    @Test
    fun keepSameId() {
        assertEquals(result.id, resultViewState.id)
    }

    @Test
    fun matchWithTitle() {
        assertEquals(result.title + "(2022)", resultViewState.title)
    }

    @Test
    fun matchWithBackdropPath() {
        assertEquals(POSTER_PATH + result.backdrop_path, resultViewState.backdrop_path)
    }

    @Test
    fun keepSamePosterPath() {
        assertEquals(result.poster_path, resultViewState.poster_path)
    }

    @Test
    fun keepSameOverView() {
        assertEquals(result.overview, resultViewState.overview)
    }

    @Test
    fun keepSameGenreIds() {
        assertEquals(result.genre_ids, resultViewState.genre_ids)
    }

    @Test
    fun matchWithReleaseDate() {
        assertEquals("2022.12.12", resultViewState.release_date)
    }

    @Test
    fun matchWithVoteAverage() {
        assertEquals("9.16", resultViewState.vote_average)
    }
}