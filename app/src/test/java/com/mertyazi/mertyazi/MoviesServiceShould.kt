package com.mertyazi.mertyazi

import com.mertyazi.mertyazi.data.remote.MoviesAPI
import com.mertyazi.mertyazi.data.remote.MoviesService
import com.mertyazi.mertyazi.data.remote.responses.NowPlayingResponse
import com.mertyazi.mertyazi.data.remote.responses.UpcomingResponse
import com.mertyazi.mertyazi.utils.BaseUnitTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class MoviesServiceShould: BaseUnitTest() {

    private lateinit var service: MoviesService
    private val api: MoviesAPI = mock()
    private val upcomingMovies: Response<UpcomingResponse> = mock()
    private val nowPlayingMovies: Response<NowPlayingResponse> = mock()

    @Test
    fun fetchUpcomingMoviesFromAPI(): Unit = runBlocking {
        service = MoviesService(api)
        service.fetchUpcomingMovies(1).first()

        verify(api, times(1)).fetchAllUpcomingMovies(1)
    }

    @Test
    fun fetchNowPlayingMoviesFromAPI(): Unit = runBlocking {
        service = MoviesService(api)
        service.fetchNowPlayingMovies().first()

        verify(api, times(1)).fetchNowPlayingMovies()
    }

    @Test
    fun convertUpcomingMoviesValuesToFlowResultAndEmitsThem(): Unit = runBlocking {
        mockSuccessfulCaseUpcomingMovies()

        TestCase.assertEquals(Result.success(upcomingMovies), service.fetchUpcomingMovies(1).first())
    }

    @Test
    fun convertNowPlayingMoviesValuesToFlowResultAndEmitsThem(): Unit = runBlocking {
        mockSuccessfulCaseNowPlayingMovies()

        TestCase.assertEquals(Result.success(nowPlayingMovies), service.fetchNowPlayingMovies().first())
    }

    @Test
    fun emitsUpcomingMoviesErrorResultWhenNetworkFails(): Unit = runBlocking {
        mockErrorCaseUpcomingMovies()

        TestCase.assertEquals(
            "Something went wrong with upcoming",
            service.fetchUpcomingMovies(1).first().exceptionOrNull()?.message
        )
    }

    @Test
    fun emitsNowPlayingMoviesErrorResultWhenNetworkFails(): Unit = runBlocking {
        mockErrorCaseNowPlayingMovies()

        TestCase.assertEquals(
            "Something went wrong with now playing",
            service.fetchNowPlayingMovies().first().exceptionOrNull()?.message
        )
    }

    private suspend fun mockErrorCaseUpcomingMovies() {
        whenever(api.fetchAllUpcomingMovies(1)).thenThrow(RuntimeException("Error upcoming movies"))

        service = MoviesService(api)
    }

    private suspend fun mockErrorCaseNowPlayingMovies() {
        whenever(api.fetchNowPlayingMovies()).thenThrow(RuntimeException("Error now playing movies"))

        service = MoviesService(api)
    }

    private suspend fun mockSuccessfulCaseUpcomingMovies() {
        whenever(api.fetchAllUpcomingMovies()).thenReturn(upcomingMovies)

        service = MoviesService(api)
    }

    private suspend fun mockSuccessfulCaseNowPlayingMovies() {
        whenever(api.fetchNowPlayingMovies()).thenReturn(nowPlayingMovies)

        service = MoviesService(api)
    }
}