package com.mertyazi.mertyazi

import com.mertyazi.mertyazi.data.local.MoviesDao
import com.mertyazi.mertyazi.data.remote.MoviesService
import com.mertyazi.mertyazi.data.remote.responses.NowPlayingResponse
import com.mertyazi.mertyazi.data.remote.responses.UpcomingResponse
import com.mertyazi.mertyazi.repositories.MoviesRepository
import com.mertyazi.mertyazi.utils.BaseUnitTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class MoviesRepositoryShould: BaseUnitTest() {

    private val service: MoviesService = mock()
    private val dao: MoviesDao = mock()
    private val upComingMovies = mock<Response<UpcomingResponse>>()
    private val nowPlayingMovies = mock<Response<NowPlayingResponse>>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getUpcomingMoviesFromService(): Unit = runBlocking {

        val repository = mockSuccessfulCaseUpcomingMovies()

        repository.getUpcomingMovies(1)

        verify(service, times(1)).fetchUpcomingMovies(1)
    }

    @Test
    fun getNowPlayingMoviesFromService(): Unit = runBlocking {

        val repository = mockSuccessfulCaseNowPlayingMovies()

        repository.getNowPlayingMovies()

        verify(service, times(1)).fetchNowPlayingMovies()
    }

    @Test
    fun propagateErrorsUpcomingMovies() = runBlocking {
        val repository = mockFailureCaseUpcomingMovies()

        TestCase.assertEquals(exception, repository.getUpcomingMovies(1).first().exceptionOrNull())
    }

    @Test
    fun propagateErrorsNowPlayingMovies() = runBlocking {
        val repository = mockFailureCaseNowPlayingMovies()

        TestCase.assertEquals(exception, repository.getNowPlayingMovies().first().exceptionOrNull())
    }

    private suspend fun mockSuccessfulCaseUpcomingMovies(): MoviesRepository {
        whenever(service.fetchUpcomingMovies(1)).thenReturn(
            flow {
                emit(Result.success(upComingMovies))
            }
        )

        return MoviesRepository(service, dao)
    }

    private suspend fun mockSuccessfulCaseNowPlayingMovies(): MoviesRepository {
        whenever(service.fetchNowPlayingMovies()).thenReturn(
            flow {
                emit(Result.success(nowPlayingMovies))
            }
        )

        return MoviesRepository(service, dao)
    }

    private suspend fun mockFailureCaseUpcomingMovies(): MoviesRepository {
        whenever(service.fetchUpcomingMovies(1)).thenReturn(
            flow {
                emit(Result.failure(exception))
            }
        )

        return MoviesRepository(service, dao)
    }

    private suspend fun mockFailureCaseNowPlayingMovies(): MoviesRepository {
        whenever(service.fetchNowPlayingMovies()).thenReturn(
            flow {
                emit(Result.failure(exception))
            }
        )

        return MoviesRepository(service, dao)
    }
}