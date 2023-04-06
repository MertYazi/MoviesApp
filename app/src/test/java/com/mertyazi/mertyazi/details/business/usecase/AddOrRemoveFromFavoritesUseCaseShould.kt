package com.mertyazi.mertyazi.details.business.usecase

import com.mertyazi.mertyazi.details.presentation.DetailsViewState
import com.mertyazi.mertyazi.favorites.business.mapper.DetailsViewStateToFavoriteMapper
import com.mertyazi.mertyazi.favorites.business.repository.FavoritesRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddOrRemoveFromFavoritesUseCaseShould {

    private val isMovieInFavoritesUseCase = mock<IsMovieInFavoritesUseCase>()
    private val favoritesRepository = mock<FavoritesRepository>()
    private val detailsViewStateToFavoriteMapper = mock<DetailsViewStateToFavoriteMapper>()
    private lateinit var useCase : AddOrRemoveFromFavoritesUseCase
    private lateinit var detailsViewState: DetailsViewState

    @Before
    fun setup(){
        useCase =
            AddOrRemoveFromFavoritesUseCase(
                isMovieInFavoritesUseCase,
                detailsViewStateToFavoriteMapper,
                favoritesRepository
            )
        detailsViewState = DetailsViewState(
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
    }

    @Test
    fun callAddMethodWhenMovieIsNotInFavorites() = runTest {
        whenever(isMovieInFavoritesUseCase.execute(any())).thenReturn(
            false
        )
        useCase.execute(
            detailsViewState
        )
        verify(favoritesRepository).addToFavorites(
            detailsViewStateToFavoriteMapper.invoke(
                detailsViewState
            )
        )
    }

    @Test
    fun callRemoveMethodWhenMovieIsInFavorites() = runTest {
        whenever(isMovieInFavoritesUseCase.execute(any())).thenReturn(
            true
        )
        useCase.execute(
            detailsViewState
        )
        verify(favoritesRepository).removeFromFavorites(
            detailsViewStateToFavoriteMapper.invoke(
                detailsViewState
            )
        )
    }
}