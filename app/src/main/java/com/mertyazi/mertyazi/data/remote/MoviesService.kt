package com.mertyazi.mertyazi.data.remote

import com.mertyazi.mertyazi.data.remote.responses.MovieDetailsResponse
import com.mertyazi.mertyazi.data.remote.responses.NowPlayingResponse
import com.mertyazi.mertyazi.data.remote.responses.UpcomingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import kotlin.Result

class MoviesService @Inject constructor(
    private val api: MoviesAPI
) {

    suspend fun fetchUpcomingMovies(page: Int): Flow<Result<Response<UpcomingResponse>>> {
        return flow {
            emit(Result.success(api.fetchAllUpcomingMovies(page)))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong with upcoming")))
        }
    }

    suspend fun fetchNowPlayingMovies(): Flow<Result<Response<NowPlayingResponse>>> {
        return flow {
            emit(Result.success(api.fetchNowPlayingMovies()))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong with now playing")))
        }
    }

    suspend fun fetchMovieDetails(movieId: String): Flow<Result<Response<MovieDetailsResponse>>> {
        return flow {
            emit(Result.success(api.fetchMovieDetails(movieId)))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong with movie details")))
        }
    }

}