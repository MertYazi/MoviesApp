package com.mertyazi.mertyazi.favorites.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mertyazi.mertyazi.MainCoroutineScopeRule
import com.mertyazi.mertyazi.favorites.business.mapper.FavoriteToFavoriteViewStateMapper
import com.mertyazi.mertyazi.favorites.business.mapper.RandomToFavoriteViewStateMapper
import com.mertyazi.mertyazi.favorites.business.repository.FavoritesRepository
import com.mertyazi.mertyazi.favorites.data.repository.database.FavoriteMovieEntity
import com.mertyazi.mertyazi.getValueForTest
import com.mertyazi.mertyazi.shared.data.repository.api.Result
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class FavoritesViewModelShould {

    @get:Rule
    var coroutinesTestRule = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository: FavoritesRepository = mock()
    private val dispatcher = StandardTestDispatcher()
    private val favoriteToFavoriteViewStateMapper: FavoriteToFavoriteViewStateMapper = mock()
    private val randomToFavoriteViewStateMapper: RandomToFavoriteViewStateMapper = mock()

    private val favorites = mock<List<FavoriteMovieEntity>>()
    private val expectedFavorites = Result.Success(favorites)

    private val randomMovie = mock<FavoriteMovieEntity>()
    private val expectedRandomMovie = Result.Success(randomMovie)

    private val exception = Result.Error(java.lang.RuntimeException("Something went wrong"))

    @Test
    fun getFavoritesFromRepository(): Unit = runBlocking {
        val viewModel = mockSuccessfulCaseForFavorites()
        viewModel.viewStateFavorites.getValueForTest()
        dispatcher.scheduler.advanceUntilIdle()
        verify(repository, Mockito.times(1)).getFavorites()
    }

    @Test
    fun getRandomMovieFromRepository(): Unit = runBlocking {
        val viewModel = mockSuccessfulCaseForRandomMovie()
        viewModel.viewStateRandom.getValueForTest()
        dispatcher.scheduler.advanceUntilIdle()
        verify(repository, Mockito.times(1)).getRandom()
    }

    @Test
    fun emitFavoritesFromRepository() = runBlocking {
        val viewModel = mockSuccessfulCaseForFavorites()
        val values = getFavoritesViewStateValues(viewModel)
        TestCase.assertEquals(values[0], FavoritesFragmentViewState.Loading)
        TestCase.assertEquals(
            values[1], FavoritesFragmentViewState.ContentFavorites(
                favoriteToFavoriteViewStateMapper.invoke(favorites).toMutableList()
            )
        )
    }

    @Test
    fun emitRandomMovieFromRepository() = runBlocking {
        val viewModel = mockSuccessfulCaseForRandomMovie()
        val values = getRandomMovieViewStateValues(viewModel)
        TestCase.assertEquals(values[0], FavoritesFragmentViewState.Loading)
        TestCase.assertEquals(
            values[1], FavoritesFragmentViewState.ContentRandom(
                randomToFavoriteViewStateMapper.invoke(randomMovie)
            )
        )
    }

    @Test
    fun emitFavoritesErrorWhenReceiveError() {
        val viewModel = mockErrorCaseForFavorites()
        val values = getFavoritesViewStateValues(viewModel)
        TestCase.assertEquals(values[0], FavoritesFragmentViewState.Loading)
        TestCase.assertEquals(values[1], FavoritesFragmentViewState.Error)
    }

    @Test
    fun emitRandomMovieErrorWhenReceiveError() {
        val viewModel = mockErrorCaseForRandomMovie()
        val values = getRandomMovieViewStateValues(viewModel)
        TestCase.assertEquals(values[0], FavoritesFragmentViewState.Loading)
        TestCase.assertEquals(values[1], FavoritesFragmentViewState.Error)
    }

    private fun getFavoritesViewStateValues(viewModel: FavoritesViewModel): MutableList<FavoritesFragmentViewState> {
        val values = mutableListOf<FavoritesFragmentViewState>()
        viewModel.viewStateFavorites.observeForever {
            values.add(it)
        }
        dispatcher.scheduler.advanceUntilIdle()
        return values
    }

    private fun getRandomMovieViewStateValues(viewModel: FavoritesViewModel): MutableList<FavoritesFragmentViewState> {
        val values = mutableListOf<FavoritesFragmentViewState>()
        viewModel.viewStateRandom.observeForever {
            values.add(it!!)
        }
        dispatcher.scheduler.advanceUntilIdle()
        return values
    }

    private fun mockSuccessfulCaseForFavorites(): FavoritesViewModel {
        runBlocking {
            whenever(repository.getFavorites()).thenReturn(
                expectedFavorites
            )
        }
        return FavoritesViewModel(
            repository,
            favoriteToFavoriteViewStateMapper,
            randomToFavoriteViewStateMapper,
            dispatcher
        )
    }

    private fun mockSuccessfulCaseForRandomMovie(): FavoritesViewModel {
        runBlocking {
            whenever(repository.getRandom()).thenReturn(
                expectedRandomMovie
            )
        }
        return FavoritesViewModel(
            repository,
            favoriteToFavoriteViewStateMapper,
            randomToFavoriteViewStateMapper,
            dispatcher
        )
    }

    private fun mockErrorCaseForFavorites(): FavoritesViewModel {
        runBlocking {
            whenever(repository.getFavorites()).thenReturn(
                exception
            )
        }
        return FavoritesViewModel(
            repository,
            favoriteToFavoriteViewStateMapper,
            randomToFavoriteViewStateMapper,
            dispatcher
        )
    }

    private fun mockErrorCaseForRandomMovie(): FavoritesViewModel {
        runBlocking {
            whenever(repository.getRandom()).thenReturn(
                exception
            )
        }
        return FavoritesViewModel(
            repository,
            favoriteToFavoriteViewStateMapper,
            randomToFavoriteViewStateMapper,
            dispatcher
        )
    }
}