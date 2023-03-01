package com.mertyazi.mertyazi.shared.data.repository.api

import com.mertyazi.mertyazi.BuildConfig
import com.mertyazi.mertyazi.shared.Constants
import com.mertyazi.mertyazi.shared.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        fun getService(): MoviesService {
            val client = OkHttpClient.Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor { chain ->
                    val url = chain
                        .request()
                        .url
                        .newBuilder()
                        .addQueryParameter(Constants.API_KEY, BuildConfig.API_KEY)
                        .build()
                    chain.proceed(chain.request().newBuilder().url(url).build())
                }
                .build()

            val build = Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return build.create(MoviesService::class.java)
        }
    }
}