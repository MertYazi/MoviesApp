package com.mertyazi.mertyazi.movies.presentation

import androidx.lifecycle.*
import com.mertyazi.mertyazi.movies.business.mapper.ResultToResultViewStateMapper
import com.mertyazi.mertyazi.shared.Constants.SLIDER_ITEM_COUNT
import com.mertyazi.mertyazi.shared.business.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import com.mertyazi.mertyazi.shared.data.repository.api.Result

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val resultToResultViewStateMapper: ResultToResultViewStateMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _viewStateUpcoming = MutableLiveData<MoviesViewState>()
    val viewStateUpcoming: LiveData<MoviesViewState>
        get() = _viewStateUpcoming

    var upcomingMoviesPage = 0
    private var upcomingMoviesResponse: UpcomingViewState? = null

    private val _viewStateNowPlaying = MutableLiveData<MoviesViewState>()
    val viewStateNowPlaying: LiveData<MoviesViewState>
        get() = _viewStateNowPlaying

    private var nowPlayingMoviesResponse: NowPlayingViewState? = null

    init {
        getUpcomingMovies()
        getNowPlayingMovies()
    }

    fun getUpcomingMovies() = viewModelScope.launch(dispatcher) {
        upcomingMoviesPage++
        _viewStateUpcoming.postValue(MoviesViewState.Loading)

        when (val result = repository.getUpcomingMovies(upcomingMoviesPage)) {
            is Result.Error -> {
                _viewStateUpcoming.postValue(MoviesViewState.Error)
            }
            is Result.Success -> {
                val upcoming = UpcomingViewState(
                    resultToResultViewStateMapper.invoke(result.data.results),
                    result.data.totalPages
                )
                if (upcomingMoviesResponse == null) {
                    upcomingMoviesResponse = upcoming
                } else {
                    val oldMovies = upcomingMoviesResponse?.results
                    val newMovies = upcoming.results
                    oldMovies?.addAll(newMovies)
                }
                _viewStateUpcoming.postValue(MoviesViewState.ContentUpcoming(
                    upcomingMoviesResponse ?: upcoming
                ))
            }
        }
    }

    private fun getNowPlayingMovies() = viewModelScope.launch(dispatcher) {
        _viewStateNowPlaying.postValue(MoviesViewState.Loading)

        when (val result = repository.getNowPlayingMovies()) {
            is Result.Error -> _viewStateNowPlaying.postValue(MoviesViewState.Error)
            is Result.Success -> {
                val nowPlaying = NowPlayingViewState(
                    if (result.data.results.size > 5) {
                        resultToResultViewStateMapper.invoke(
                            result.data.results
                        ).subList(0, SLIDER_ITEM_COUNT).toMutableList()
                    } else {
                        resultToResultViewStateMapper.invoke(
                            result.data.results
                        )
                    },
                    result.data.totalPages
                )
                if (nowPlayingMoviesResponse == null) {
                    nowPlayingMoviesResponse = nowPlaying
                } else {
                    val oldMovies = nowPlayingMoviesResponse?.results
                    val newMovies = nowPlaying.results
                    oldMovies?.addAll(newMovies)
                }
                _viewStateNowPlaying.postValue(MoviesViewState.ContentNowPlaying(
                    nowPlayingMoviesResponse ?: nowPlaying
                ))
            }
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
}