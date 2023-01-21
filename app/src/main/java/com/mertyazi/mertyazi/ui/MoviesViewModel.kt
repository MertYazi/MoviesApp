package com.mertyazi.mertyazi.ui

import android.util.Log
import androidx.lifecycle.*
import com.mertyazi.mertyazi.data.remote.responses.MovieDetailsResponse
import com.mertyazi.mertyazi.repositories.MoviesRepository
import com.mertyazi.mertyazi.data.remote.responses.NowPlayingResponse
import com.mertyazi.mertyazi.data.remote.responses.UpcomingResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val repository: MoviesRepository
): ViewModel() {

    var movieId: String = ""

    val loader = MutableLiveData<Boolean>()

    val favorite = MutableLiveData<Boolean>()

    val randomMovie = MutableLiveData<MovieDetailsResponse>()

    val upcomingMovies: MutableLiveData<UpcomingResponse> = MutableLiveData()
    var upcomingMoviesPage = 0
    private var upcomingMoviesResponse: UpcomingResponse? = null

    val nowPlayingMovies: MutableLiveData<NowPlayingResponse> = MutableLiveData()
    private var nowPlayingMoviesResponse: NowPlayingResponse? = null

    init {
        getUpcomingMovies()
        getNowPlayingMovies()
        getRandomMovie()
    }

    fun getUpcomingMovies() = viewModelScope.safeLaunch {
        upcomingMoviesPage++
        loader.postValue(true)
        repository.getUpcomingMovies(upcomingMoviesPage).collect { result ->
            if (result.isSuccess) {
                result.getOrNull()!!.body()?.let {
                    if (upcomingMoviesResponse == null) {
                        upcomingMoviesResponse = it
                    } else {
                        val oldMovies = upcomingMoviesResponse?.results
                        val newMovies = it.results
                        oldMovies?.addAll(newMovies)
                    }
                    upcomingMovies.postValue(upcomingMoviesResponse ?: it)
                }
            }
            loader.postValue(false)
        }
    }

    private fun getNowPlayingMovies() = viewModelScope.safeLaunch {
        loader.postValue(true)
        repository.getNowPlayingMovies().collect { result ->
            if (result.isSuccess) {
                result.getOrNull()!!.body()?.let {
                    nowPlayingMovies.postValue(it)
                }
            }
            loader.postValue(false)
        }
    }

    fun isFavorite(id: String) = viewModelScope.safeLaunch {
        loader.postValue(true)
        val movie = repository.isMovieFavorite(id)
        if (movie != null) {
            favorite.postValue(true)
        } else {
            favorite.postValue(false)
        }
        loader.postValue(false)
    }

    fun getRandomMovie() = viewModelScope.safeLaunch {
        randomMovie.postValue(repository.getRandomMovie())
    }

    val movieDetails = liveData {
        loader.postValue(true)
        emitSource(repository.getMovieDetails(movieId)
            .onEach {
                loader.postValue(false)
            }
            .asLiveData())
    }

    fun refreshUpcomingMovies() {
        upcomingMoviesPage = 0
        upcomingMoviesResponse = null
        getUpcomingMovies()
    }

    fun refreshNowPlayingMovies() {
        nowPlayingMoviesResponse = null
        getNowPlayingMovies()
    }

    fun getFavoriteMovies() = repository.getFavoriteMovies()

    fun insertFavoriteMovie(movieDetails: MovieDetailsResponse) = viewModelScope.safeLaunch {
        repository.insertFavoriteMovie(movieDetails)
    }

    fun deleteFavoriteMovie(movieDetails: MovieDetailsResponse) = viewModelScope.safeLaunch {
        repository.deleteFavoriteMovie(movieDetails)
    }

    private fun CoroutineScope.safeLaunch(block: suspend CoroutineScope.() -> Unit): Job {
        return this.launch {
            try {
                block()
            } catch (_: CancellationException) {
            } catch (e: Exception) {
                Log.e("TAG","Coroutine error", e)
            }
        }
    }

}