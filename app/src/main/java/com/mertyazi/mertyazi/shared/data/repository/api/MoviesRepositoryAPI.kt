package com.mertyazi.mertyazi.shared.data.repository.api

import android.util.Log
import com.mertyazi.mertyazi.movies.business.entities.NowPlaying
import com.mertyazi.mertyazi.movies.business.entities.Upcoming
import com.mertyazi.mertyazi.details.business.entities.MovieDetails
import com.mertyazi.mertyazi.details.business.mapper.DetailsEntityToDetailsMapper
import com.mertyazi.mertyazi.movies.business.mapper.ResultEntityToResultMapper
import com.mertyazi.mertyazi.shared.business.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryAPI @Inject constructor(
    private val service: MoviesService,
    private val resultEntityToResultMapper: ResultEntityToResultMapper,
    private val detailsEntityToDetailsMapper: DetailsEntityToDetailsMapper,
): MoviesRepository {
    override suspend fun getUpcomingMovies(page: Int): Result<Upcoming> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.fetchAllUpcomingMovies(page)
                val data = Upcoming(
                    response.results.map {
                        resultEntityToResultMapper.invoke(it)
                    }.toMutableList(),
                    response.total_pages
                )
                if (data.results.isNotEmpty()) {
                    Result.Success(data)
                } else {
                    Result.Error(IllegalStateException("Empty upcoming movies list"))
                }
            } catch (exception: Exception) {
                Log.e("NetworkLayer", exception.message, exception)
                    Result.Error(exception)
            }
        }
    }

    override suspend fun getNowPlayingMovies(): Result<NowPlaying> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.fetchNowPlayingMovies()
                val data = NowPlaying(
                    response.results.map {
                        resultEntityToResultMapper.invoke(it)
                    }.toMutableList(),
                    response.total_pages
                )
                if (data.results.isNotEmpty()) {
                    Result.Success(data)
                } else {
                    Result.Error(IllegalStateException("Empty now playing movies list"))
                }
            } catch (exception: Exception) {
                Log.e("NetworkLayer", exception.message, exception)
                Result.Error(exception)
            }
        }
    }

    override suspend fun getMovieDetails(movieId: String): Result<MovieDetails> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.fetchMovieDetails(movieId)
                val data = detailsEntityToDetailsMapper.invoke(response)
                if (data.id != null) {
                    Result.Success(data)
                } else {
                    Result.Error(IllegalStateException("Empty movie details"))
                }
            } catch (exception: Exception) {
                Log.e("NetworkLayer", exception.message, exception)
                Result.Error(exception)
            }
        }
    }
}