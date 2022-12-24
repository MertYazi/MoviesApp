package com.mertyazi.mertyazi.di

import com.jakewharton.espresso.OkHttp3IdlingResource
import com.mertyazi.mertyazi.data.remote.MoviesAPI
import com.mertyazi.mertyazi.other.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
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
}