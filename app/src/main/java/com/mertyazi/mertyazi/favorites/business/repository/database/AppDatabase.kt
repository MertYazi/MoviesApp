package com.mertyazi.mertyazi.favorites.business.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mertyazi.mertyazi.favorites.data.repository.database.FavoriteMovieEntity
import com.mertyazi.mertyazi.favorites.data.repository.database.FavoritesDAO

@Database(entities = [FavoriteMovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDAO
}