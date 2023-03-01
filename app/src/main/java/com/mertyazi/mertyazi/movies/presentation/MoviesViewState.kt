package com.mertyazi.mertyazi.movies.presentation

sealed class MoviesViewState {
    object Loading : MoviesViewState()
    object Error : MoviesViewState()
    data class ContentUpcoming(val upcoming: UpcomingViewState) : MoviesViewState()
    data class ContentNowPlaying(val nowPlaying: NowPlayingViewState) : MoviesViewState()
}