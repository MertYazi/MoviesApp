package com.mertyazi.mertyazi.favorites.data.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FavoriteMovieEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "movie_title") val movieTitle : String,
    @ColumnInfo(name = "movie_poster") val moviePoster : String,
    @ColumnInfo(name = "movie_poster_with_title") val moviePosterWithTitle : String
)