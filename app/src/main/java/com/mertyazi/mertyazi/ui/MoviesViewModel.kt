package com.mertyazi.mertyazi.ui

import android.util.Log
import androidx.lifecycle.*
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

    val loader = MutableLiveData<Boolean>()

    var movieId: String = ""

    val upcomingMovies: MutableLiveData<UpcomingResponse> = MutableLiveData()
    var upcomingMoviesPage = 0
    private var upcomingMoviesResponse: UpcomingResponse? = null

    val nowPlayingMovies: MutableLiveData<NowPlayingResponse> = MutableLiveData()
    private var nowPlayingMoviesResponse: NowPlayingResponse? = null

    init {
        getUpcomingMovies()
        getNowPlayingMovies()
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

    fun refreshUpcomingMovies() {
        upcomingMoviesPage = 0
        upcomingMoviesResponse = null
        getUpcomingMovies()
    }

    fun refreshNowPlayingMovies() {
        nowPlayingMoviesResponse = null
        getNowPlayingMovies()
    }

    val movieDetails = liveData {
        loader.postValue(true)
        emitSource(repository.getMovieDetails(movieId)
            .onEach {
                loader.postValue(false)
            }
            .asLiveData())
    }

    private fun CoroutineScope.safeLaunch(block: suspend CoroutineScope.() -> Unit): Job {
        return this.launch {
            try {
                block()
            } catch (ce: CancellationException) {
                // You can ignore or log this exception
            } catch (e: Exception) {
                // Here it's better to at least log the exception
                Log.e("TAG","Coroutine error", e)
            }
        }
    }

}