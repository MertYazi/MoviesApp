package com.mertyazi.mertyazi.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.mertyazi.mertyazi.details.business.mapper.DetailsEntityToDetailsMapper
import com.mertyazi.mertyazi.shared.presentation.DateFormatter
import com.mertyazi.mertyazi.shared.presentation.DayMonthYearFormatter
import com.mertyazi.mertyazi.favorites.business.repository.database.AppDatabase
import com.mertyazi.mertyazi.favorites.business.repository.FavoritesRepository
import com.mertyazi.mertyazi.favorites.data.repository.FavoritesDatabaseRepository
import com.mertyazi.mertyazi.favorites.data.repository.database.FavoritesDAO
import com.mertyazi.mertyazi.movies.business.mapper.ResultEntityToResultMapper
import com.mertyazi.mertyazi.shared.business.repository.MoviesRepository
import com.mertyazi.mertyazi.shared.data.repository.api.ApiClient
import com.mertyazi.mertyazi.shared.data.repository.api.MoviesRepositoryAPI
import com.mertyazi.mertyazi.shared.data.repository.api.MoviesService
import com.mertyazi.mertyazi.shared.presentation.ImdbScoreFormatter
import com.mertyazi.mertyazi.shared.presentation.TwoDigitsFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import javax.inject.Singleton

val client = OkHttpClient()
val idlingResource = OkHttp3IdlingResource.create("okhttp", client)

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
class MoviesModule {

    @Provides
    fun providesMoviesService(): MoviesService = ApiClient.getService()

    @Provides
    fun providesMoviesRepositoryAPI(
        service: MoviesService,
        resultEntityToResultMapper: ResultEntityToResultMapper,
        detailsEntityToDetailsMapper: DetailsEntityToDetailsMapper
    ): MoviesRepositoryAPI = MoviesRepositoryAPI(
        service,
        resultEntityToResultMapper,
        detailsEntityToDetailsMapper)

    @Provides
    fun providesMoviesRepository(
        moviesRepositoryAPI: MoviesRepositoryAPI
    ): MoviesRepository = moviesRepositoryAPI

    @Provides
    fun providesFavoritesRepository(
        databaseRepository: FavoritesDatabaseRepository
    ): FavoritesRepository = databaseRepository

    @Provides
    fun providesFavoritesDatabaseRepository(databaseDAO: FavoritesDAO): FavoritesDatabaseRepository {
        return FavoritesDatabaseRepository(databaseDAO)
    }

    @Provides
    fun providesWishListDAO(
        @ApplicationContext context: Context
    ): FavoritesDAO {
        val db = Room.databaseBuilder(
            context, AppDatabase::class.java,
            "movies-database"
        ).build()
        return db.favoritesDao()
    }

    @Provides
    @Singleton
    fun providesImdbScoreFormatter(): ImdbScoreFormatter {
        return TwoDigitsFormatter
    }

    @Provides
    @Singleton
    fun providesDateFormatter(): DateFormatter {
        return DayMonthYearFormatter
    }

    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}