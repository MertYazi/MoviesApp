package com.mertyazi.mertyazi.shared.data.repository.api

import com.mertyazi.mertyazi.movies.data.entities.NowPlayingEntity
import com.mertyazi.mertyazi.movies.data.entities.UpcomingEntity
import com.mertyazi.mertyazi.shared.Constants
import com.mertyazi.mertyazi.details.data.entities.MovieDetailsEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("movie/upcoming")
    suspend fun fetchAllUpcomingMovies(
        @Query("page") page: Int = 1
    ): UpcomingEntity

    @GET("movie/now_playing")
    suspend fun fetchNowPlayingMovies(
    ): NowPlayingEntity

    @GET("movie/{movie_id}")
    suspend fun fetchMovieDetails(
        @Path(Constants.MOVIE_ID) movieId: String
    ): MovieDetailsEntity
}