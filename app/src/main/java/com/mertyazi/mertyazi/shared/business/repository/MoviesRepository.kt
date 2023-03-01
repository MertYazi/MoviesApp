package com.mertyazi.mertyazi.shared.business.repository

import com.mertyazi.mertyazi.details.business.entities.MovieDetails
import com.mertyazi.mertyazi.movies.business.entities.NowPlaying
import com.mertyazi.mertyazi.movies.business.entities.Upcoming
import com.mertyazi.mertyazi.shared.data.repository.api.Result

interface MoviesRepository {

    suspend fun getUpcomingMovies(page: Int): Result<Upcoming>

    suspend fun getNowPlayingMovies(): Result<NowPlaying>

    suspend fun getMovieDetails(movieId: String): Result<MovieDetails>

}