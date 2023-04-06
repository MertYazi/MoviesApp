package com.mertyazi.mertyazi.favorites.data.repository

import com.mertyazi.mertyazi.favorites.data.repository.database.FavoriteMovieEntity
import com.mertyazi.mertyazi.favorites.data.repository.database.FavoritesDAO
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesDatabaseRepositoryShould {

    private lateinit var repository: FavoritesDatabaseRepository
    private val dao = mock<FavoritesDAO>()

    @Before
    fun setup() {
        repository = FavoritesDatabaseRepository(dao)
    }

    @Test
    fun returnsTrueWhenGivenMovieIsSavedIntoDatabase() = runTest {
        whenever(dao.isMovieFavorite("1")).thenReturn(
            FavoriteMovieEntity(
                "1",
                "title",
                "poster",
                "posterWithTitle"
            )
        )
        assertTrue(repository.isFavorite("1"))
    }

    @Test
    fun returnsFalseWhenGivenMovieIsNotSavedIntoDatabase() = runTest {
        whenever(dao.isMovieFavorite("1")).thenReturn(
            null
        )
        assertFalse(repository.isFavorite("1"))
    }
}