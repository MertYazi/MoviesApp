package com.mertyazi.mertyazi.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.mertyazi.mertyazi.data.local.MoviesDao
import com.mertyazi.mertyazi.data.local.MoviesDatabase
import com.mertyazi.mertyazi.data.remote.MoviesAPI
import com.mertyazi.mertyazi.other.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val client = OkHttpClient()
val idlingResource = OkHttp3IdlingResource.create("okhttp", client)

@Module
@InstallIn(FragmentComponent::class)
class MoviesModule {

    @Provides
    fun moviesAPI(retrofit: Retrofit) = retrofit.create(MoviesAPI::class.java)

    @Provides
    fun retrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun moviesDao(moviesDatabase: MoviesDatabase): MoviesDao {
        return moviesDatabase.getMoviesDao()
    }

    @Provides
    fun moviesDatabase(@ApplicationContext appContext: Context): MoviesDatabase {
        return Room.databaseBuilder(
            appContext,
            MoviesDatabase::class.java,
            "movies_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}