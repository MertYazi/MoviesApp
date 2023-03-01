package com.mertyazi.mertyazi.favorites.data.repository.database

import androidx.room.*

@Dao
interface FavoritesDAO {

    @Query("SELECT * FROM favoritemovieentity WHERE id=:id")
    fun isMovieFavorite(id: String): FavoriteMovieEntity?

    @Query("SELECT * FROM favoritemovieentity")
    fun getAllFavoriteMovies(): List<FavoriteMovieEntity>

    @Query("SELECT * FROM favoritemovieentity ORDER BY RANDOM() LIMIT 1")
    fun getRandomMovie(): FavoriteMovieEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovieToFavorites(movie:  FavoriteMovieEntity)

    @Delete
    fun removeMovieFromFavorites(movie: FavoriteMovieEntity)
}