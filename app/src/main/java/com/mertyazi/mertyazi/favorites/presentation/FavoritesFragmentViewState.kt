package com.mertyazi.mertyazi.favorites.presentation

sealed class FavoritesFragmentViewState {
    object Loading : FavoritesFragmentViewState()
    object Error : FavoritesFragmentViewState()
    data class ContentFavorites(
        val favorites: MutableList<FavoriteMovieViewState>) : FavoritesFragmentViewState()
    data class ContentRandom(
        val random: FavoriteMovieViewState?) : FavoritesFragmentViewState()
}