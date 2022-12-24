package com.mertyazi.mertyazi.data.remote

import com.mertyazi.mertyazi.BuildConfig
import com.mertyazi.mertyazi.data.remote.responses.MovieDetailsResponse
import com.mertyazi.mertyazi.data.remote.responses.NowPlayingResponse
import com.mertyazi.mertyazi.data.remote.responses.UpcomingResponse
import com.mertyazi.mertyazi.other.Constants.API_KEY
import com.mertyazi.mertyazi.other.Constants.MOVIE_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {

    @GET("movie/upcoming")
    suspend fun fetchAllUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query(API_KEY) apiKey: String = BuildConfig.API_KEY
    ): Response<UpcomingResponse>

    @GET("movie/now_playing")
    suspend fun fetchNowPlayingMovies(
        @Query(API_KEY) apiKey: String = BuildConfig.API_KEY
    ): Response<NowPlayingResponse>

    @GET("movie/{movie_id}")
    suspend fun fetchMovieDetails(
        @Path(MOVIE_ID) movieId: String,
        @Query(API_KEY) apiKey: String = BuildConfig.API_KEY
    ): Response<MovieDetailsResponse>
}