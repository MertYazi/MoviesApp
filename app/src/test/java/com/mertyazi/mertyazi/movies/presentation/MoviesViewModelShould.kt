package com.mertyazi.mertyazi.movies.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mertyazi.mertyazi.MainCoroutineScopeRule
import com.mertyazi.mertyazi.getValueForTest
import com.mertyazi.mertyazi.movies.business.entities.NowPlaying
import com.mertyazi.mertyazi.movies.business.entities.Upcoming
import com.mertyazi.mertyazi.movies.business.mapper.ResultToResultViewStateMapper
import com.mertyazi.mertyazi.movies.presentation.MoviesViewModel
import com.mertyazi.mertyazi.movies.presentation.MoviesViewState
import com.mertyazi.mertyazi.movies.presentation.NowPlayingViewState
import com.mertyazi.mertyazi.movies.presentation.UpcomingViewState
import com.mertyazi.mertyazi.shared.business.repository.MoviesRepository
import com.mertyazi.mertyazi.shared.data.repository.api.Result
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class MoviesViewModelShould {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository: MoviesRepository = mock()
    private val dispatcher = StandardTestDispatcher()
    private val resultToResultViewStateMapper = mock<ResultToResultViewStateMapper>()

    private val upcoming = mock<Upcoming>()
    private val expectedUpcoming = Result.Success(upcoming)

    private val nowPlaying = mock<NowPlaying>()
    private val expectedNowPlaying = Result.Success(nowPlaying)

    private val exception = Result.Error(java.lang.RuntimeException("Something went wrong"))

    @Test
    fun getUpcomingFromRepository(): Unit = runBlocking {
        val viewModel = mockSuccessfulCaseForUpcomingMovies()
        viewModel.viewStateUpcoming.getValueForTest()
        dispatcher.scheduler.advanceUntilIdle()
        verify(repository, Mockito.times(1)).getUpcomingMovies(1)
    }

    @Test
    fun getNowPlayingFromRepository(): Unit = runBlocking {
        val viewModel = mockSuccessfulCaseForNowPlayingMovies()
        viewModel.viewStateNowPlaying.getValueForTest()
        dispatcher.scheduler.advanceUntilIdle()
        verify(repository, Mockito.times(1)).getNowPlayingMovies()
    }

    @Test
    fun emitUpcomingFromRepository() = runBlocking {
        val viewModel = mockSuccessfulCaseForUpcomingMovies()
        val values = getUpcomingViewStateValues(viewModel)
        assertEquals(values[0], MoviesViewState.Loading)
        assertEquals(values[1], MoviesViewState.ContentUpcoming(
            UpcomingViewState(
                resultToResultViewStateMapper.invoke(
                    upcoming.results
                ),
                0
            )
        ))
    }

    @Test
    fun emitNowPlayingFromRepository() = runBlocking {
        val viewModel = mockSuccessfulCaseForNowPlayingMovies()
        val values = getNowPlayingViewStateValues(viewModel)
        assertEquals(values[0], MoviesViewState.Loading)
        assertEquals(values[1], MoviesViewState.ContentNowPlaying(
            NowPlayingViewState(
                resultToResultViewStateMapper.invoke(
                    nowPlaying.results
                ),
                0
            )
        ))
    }

    @Test
    fun emitUpcomingErrorWhenReceiveError() {
        val viewModel = mockErrorCaseForUpcomingMovies()
        val values = getUpcomingViewStateValues(viewModel)
        assertEquals(values[0], MoviesViewState.Loading)
        assertEquals(values[1], MoviesViewState.Error)
    }

    @Test
    fun emitNowPlayingErrorWhenReceiveError() {
        val viewModel = mockErrorCaseForNowPlayingMovies()
        val values = getNowPlayingViewStateValues(viewModel)
        assertEquals(values[0], MoviesViewState.Loading)
        assertEquals(values[1], MoviesViewState.Error)
    }

    private fun getUpcomingViewStateValues(viewModel: MoviesViewModel): MutableList<MoviesViewState> {
        val values = mutableListOf<MoviesViewState>()
        viewModel.viewStateUpcoming.observeForever {
            values.add(it)
        }
        dispatcher.scheduler.advanceUntilIdle()
        return values
    }

    private fun getNowPlayingViewStateValues(viewModel: MoviesViewModel): MutableList<MoviesViewState> {
        val values = mutableListOf<MoviesViewState>()
        viewModel.viewStateNowPlaying.observeForever {
            values.add(it)
        }
        dispatcher.scheduler.advanceUntilIdle()
        return values
    }

    private fun mockSuccessfulCaseForUpcomingMovies(): MoviesViewModel {
        runBlocking {
            whenever(repository.getUpcomingMovies(1)).thenReturn(
                expectedUpcoming
            )
        }
        return MoviesViewModel(repository, resultToResultViewStateMapper, dispatcher)
    }

    private fun mockErrorCaseForUpcomingMovies(): MoviesViewModel {
        runBlocking {
            whenever(repository.getUpcomingMovies(1)).thenReturn(
                exception
            )
        }
        return MoviesViewModel(repository, resultToResultViewStateMapper, dispatcher)
    }

    private fun mockSuccessfulCaseForNowPlayingMovies(): MoviesViewModel {
        runBlocking {
            whenever(repository.getNowPlayingMovies()).thenReturn(
                expectedNowPlaying
            )
        }
        return MoviesViewModel(repository, resultToResultViewStateMapper, dispatcher)
    }

    private fun mockErrorCaseForNowPlayingMovies(): MoviesViewModel {
        runBlocking {
            whenever(repository.getNowPlayingMovies()).thenReturn(
                exception
            )
        }
        return MoviesViewModel(repository, resultToResultViewStateMapper, dispatcher)
    }
}