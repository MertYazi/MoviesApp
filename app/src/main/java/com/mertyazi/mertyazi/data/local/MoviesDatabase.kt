package com.mertyazi.mertyazi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mertyazi.mertyazi.data.remote.responses.MovieDetailsResponse

@Database(entities = [MovieDetailsResponse::class], version = 1)
@TypeConverters(Converters::class)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun getMoviesDao(): MoviesDao
}