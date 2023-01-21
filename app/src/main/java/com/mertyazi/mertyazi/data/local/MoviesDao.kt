package com.mertyazi.mertyazi.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mertyazi.mertyazi.data.remote.responses.MovieDetailsResponse

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movieDetails: MovieDetailsResponse): Long

    @Query("SELECT * from favoriteMovies")
    fun getFavoriteMovies(): LiveData<List<MovieDetailsResponse>>

    @Query("SELECT * FROM favoriteMovies WHERE id=:id")
    suspend fun isMovieFavorite(id: String) : MovieDetailsResponse?

    @Query("SELECT * FROM favoriteMovies ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomMovie() : MovieDetailsResponse?

    @Delete
    suspend fun deleteFavoriteMovie(movieDetails: MovieDetailsResponse)
}