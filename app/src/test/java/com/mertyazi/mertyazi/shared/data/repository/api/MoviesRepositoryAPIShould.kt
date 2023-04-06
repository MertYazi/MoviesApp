package com.mertyazi.mertyazi.shared.data.repository.api

import com.mertyazi.mertyazi.details.business.mapper.DetailsEntityToDetailsMapper
import com.mertyazi.mertyazi.movies.business.mapper.ResultEntityToResultMapper
import com.mertyazi.mertyazi.movies.data.entities.DatesEntity
import com.mertyazi.mertyazi.movies.data.entities.ResultEntity
import com.mertyazi.mertyazi.movies.data.entities.UpcomingEntity
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesRepositoryAPIShould {

    private lateinit var repository: MoviesRepositoryAPI
    private val service = mock<MoviesService>()
    private val resultEntityToResultMapper: ResultEntityToResultMapper = mock()
    private val detailsEntityToDetailsMapper: DetailsEntityToDetailsMapper = mock()

    @Before
    fun setup() {
        repository = MoviesRepositoryAPI(
            service,
            resultEntityToResultMapper,
            detailsEntityToDetailsMapper
        )
    }

    @Test
    fun correctMapsEntityIntoBusinessObjects() = runTest {
        whenever(service.fetchAllUpcomingMovies(1)).thenReturn(
            UpcomingEntity(
                DatesEntity(
                    "max",
                    "min"
                ),
                1,
                (0..2).map {
                    ResultEntity(
                        false,
                        "backdrop_path",
                        listOf(),
                        it,
                        "original_language",
                        "original_title",
                        "overview",
                        1.0,
                        "poster_path",
                        "release_date",
                        "title",
                        false,
                        7.74,
                        185
                    )
                }.toMutableList(),
                1,
                1
            )
        )

        val products = repository.getUpcomingMovies(1) as Result.Success

        assertEquals(products.data.results.size, 3)
        assertEquals(products.data.totalPages, 1)
    }
}