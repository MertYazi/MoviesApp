package com.mertyazi.mertyazi.repositories

import com.mertyazi.mertyazi.data.local.MoviesDao
import com.mertyazi.mertyazi.data.remote.MoviesService
import com.mertyazi.mertyazi.data.remote.responses.MovieDetailsResponse
import com.mertyazi.mertyazi.data.remote.responses.NowPlayingResponse
import com.mertyazi.mertyazi.data.remote.responses.UpcomingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject
import kotlin.Result

class MoviesRepository @Inject constructor(
    private val service: MoviesService,
    private val moviesDao: MoviesDao
) {
    suspend fun getUpcomingMovies(page: Int): Flow<Result<Response<UpcomingResponse>>> =
        service.fetchUpcomingMovies(page).map {
            if (it.isSuccess)
                Result.success(it.getOrNull()!!)
            else
                Result.failure(it.exceptionOrNull()!!)
        }

    suspend fun getNowPlayingMovies(): Flow<Result<Response<NowPlayingResponse>>> =
        service.fetchNowPlayingMovies().map {
            if (it.isSuccess)
                Result.success(it.getOrNull()!!)
            else
                Result.failure(it.exceptionOrNull()!!)
        }

    suspend fun getMovieDetails(movieId: String): Flow<Result<Response<MovieDetailsResponse>>> =
        service.fetchMovieDetails(movieId).map {
            if (it.isSuccess)
                Result.success(it.getOrNull()!!)
            else
                Result.failure(it.exceptionOrNull()!!)
        }

    fun getFavoriteMovies() = moviesDao.getFavoriteMovies()

    suspend fun insertFavoriteMovie(movieDetails: MovieDetailsResponse) =
        moviesDao.insertFavoriteMovie(movieDetails)

    suspend fun isMovieFavorite(id: String): MovieDetailsResponse? =
        moviesDao.isMovieFavorite(id)

    suspend fun getRandomMovie() = moviesDao.getRandomMovie()

    suspend fun deleteFavoriteMovie(movieDetails: MovieDetailsResponse) =
        moviesDao.deleteFavoriteMovie(movieDetails)
}