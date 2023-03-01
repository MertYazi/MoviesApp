package com.mertyazi.mertyazi.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertyazi.mertyazi.favorites.business.mapper.FavoriteToFavoriteViewStateMapper
import com.mertyazi.mertyazi.favorites.business.repository.FavoritesRepository
import com.mertyazi.mertyazi.favorites.business.mapper.RandomToFavoriteViewStateMapper
import com.mertyazi.mertyazi.shared.data.repository.api.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository,
    private val favoriteToFavoriteViewStateMapper: FavoriteToFavoriteViewStateMapper,
    private val randomToFavoriteViewStateMapper: RandomToFavoriteViewStateMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _viewStateFavorites = MutableLiveData<FavoritesFragmentViewState>()
    val viewStateFavorites: LiveData<FavoritesFragmentViewState>
        get() = _viewStateFavorites

    private val _viewStateRandom = MutableLiveData<FavoritesFragmentViewState?>()
    val viewStateRandom: LiveData<FavoritesFragmentViewState?>
        get() = _viewStateRandom

    init {
        getFavoriteMovies()
        getRandomMovie()
    }

    fun getFavoriteMovies() = viewModelScope.launch(dispatcher) {
        _viewStateFavorites.postValue(FavoritesFragmentViewState.Loading)

        when (val result = repository.getFavorites()) {
            is Result.Error -> _viewStateFavorites.postValue(FavoritesFragmentViewState.Error)
            is Result.Success -> {
                val favorites = favoriteToFavoriteViewStateMapper.invoke(result.data).toMutableList()
                _viewStateFavorites.postValue(FavoritesFragmentViewState.ContentFavorites(
                    favorites
                ))
            }
        }
    }

    fun getRandomMovie() = viewModelScope.launch(dispatcher) {
        _viewStateRandom.postValue(FavoritesFragmentViewState.Loading)

        when (val result = repository.getRandom()) {
            is Result.Error -> _viewStateRandom.postValue(FavoritesFragmentViewState.Error)
            is Result.Success -> {
                val random = result.data?.let {
                    randomToFavoriteViewStateMapper.invoke(it)
                }
                _viewStateRandom.postValue(FavoritesFragmentViewState.ContentRandom(
                    random
                ))
            }
        }
    }
}