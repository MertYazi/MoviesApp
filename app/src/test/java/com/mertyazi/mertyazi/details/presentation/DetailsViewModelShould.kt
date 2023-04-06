package com.mertyazi.mertyazi.details.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mertyazi.mertyazi.MainCoroutineScopeRule
import com.mertyazi.mertyazi.details.business.entities.MovieDetails
import com.mertyazi.mertyazi.details.business.mapper.DetailsToDetailsViewStateMapper
import com.mertyazi.mertyazi.details.business.usecase.AddOrRemoveFromFavoritesUseCase
import com.mertyazi.mertyazi.details.business.usecase.IsMovieInFavoritesUseCase
import com.mertyazi.mertyazi.getValueForTest
import com.mertyazi.mertyazi.shared.business.repository.MoviesRepository
import com.mertyazi.mertyazi.shared.data.repository.api.Result
import com.nhaarman.mockitokotlin2.any
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
class DetailsViewModelShould {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository: MoviesRepository = mock()
    private val dispatcher = StandardTestDispatcher()
    private val isMovieInFavoritesUseCase: IsMovieInFavoritesUseCase = mock()
    private val addOrRemoveFromFavoritesUseCase: AddOrRemoveFromFavoritesUseCase = mock()
    private val detailsToDetailsViewStateMapper: DetailsToDetailsViewStateMapper = mock()

    private val exception = Result.Error(java.lang.RuntimeException("Something went wrong"))

    @Test
    fun getDetailsFromRepository(): Unit = runBlocking {
        val viewModel = mockSuccessfulCaseForMovieDetails()
        viewModel.getMovieDetails("1")
        viewModel.viewStateMovieDetails.getValueForTest()
        dispatcher.scheduler.advanceUntilIdle()
        verify(repository, Mockito.times(1)).getMovieDetails("1")
    }

    @Test
    fun emitDetailsFromRepository() = runBlocking {
        val viewModel = mockSuccessfulCaseForMovieDetails()
        val values = getMovieDetailsViewStateValues(viewModel)
        assertEquals(values[0], MovieDetailsViewState.Loading)
        assertEquals(
            values[1], MovieDetailsViewState.ContentMovieDetails(
                DetailsViewState(
                    "backdrop_path",
                    listOf(),
                    "homepage",
                    1,
                    "imdb_id",
                    "overview",
                    "poster_path",
                    "release_date",
                    90,
                    "status",
                    "title",
                    "0.0",
                    true
                )
            )
        )
    }

    @Test
    fun emitDetailsErrorWhenReceiveError() {
        val viewModel = mockErrorCaseForMovieDetails()
        val values = getMovieDetailsViewStateValues(viewModel)
        assertEquals(values[0], MovieDetailsViewState.Loading)
        assertEquals(values[1], MovieDetailsViewState.Error)
    }

    private fun getMovieDetailsViewStateValues(viewModel: DetailsViewModel): MutableList<MovieDetailsViewState> {
        val values = mutableListOf<MovieDetailsViewState>()
        viewModel.viewStateMovieDetails.observeForever {
            values.add(it)
        }
        viewModel.getMovieDetails("1")
        dispatcher.scheduler.advanceUntilIdle()
        return values
    }

    private fun mockSuccessfulCaseForMovieDetails(): DetailsViewModel {
        runBlocking {
            whenever(repository.getMovieDetails("1")).thenReturn(
                Result.Success(
                    MovieDetails(
                        "backdrop_path",
                        listOf(),
                        "homepage",
                        1,
                        "imdb_id",
                        "overview",
                        "poster_path",
                        "release_date",
                        90,
                        "status",
                        "title",
                        0.0,
                        false
                    )
                )
            )
            whenever(detailsToDetailsViewStateMapper.invoke(any())).thenReturn(
                DetailsViewState(
                    "backdrop_path",
                    listOf(),
                    "homepage",
                    1,
                    "imdb_id",
                    "overview",
                    "poster_path",
                    "release_date",
                    90,
                    "status",
                    "title",
                    "0.0",
                    false
                )
            )
            whenever(isMovieInFavoritesUseCase.execute("1")).thenReturn(
                true
            )
        }
        return DetailsViewModel(
            repository,
            isMovieInFavoritesUseCase,
            addOrRemoveFromFavoritesUseCase,
            detailsToDetailsViewStateMapper,
            dispatcher
        )
    }

    private fun mockErrorCaseForMovieDetails(): DetailsViewModel {
        runBlocking {
            whenever(repository.getMovieDetails("1")).thenReturn(
                exception
            )
        }
        return DetailsViewModel(
            repository,
            isMovieInFavoritesUseCase,
            addOrRemoveFromFavoritesUseCase,
            detailsToDetailsViewStateMapper,
            dispatcher
        )
    }
}