package com.mertyazi.mertyazi.details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertyazi.mertyazi.details.business.usecase.AddOrRemoveFromFavoritesUseCase
import com.mertyazi.mertyazi.details.business.mapper.DetailsToDetailsViewStateMapper
import com.mertyazi.mertyazi.details.business.usecase.IsMovieInFavoritesUseCase
import com.mertyazi.mertyazi.shared.business.repository.MoviesRepository
import com.mertyazi.mertyazi.shared.data.repository.api.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val isMovieInFavoritesUseCase: IsMovieInFavoritesUseCase,
    private val addOrRemoveFromFavoritesUseCase: AddOrRemoveFromFavoritesUseCase,
    private val detailsToDetailsViewStateMapper: DetailsToDetailsViewStateMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _viewStateMovieDetails = MutableLiveData<MovieDetailsViewState>()
    val viewStateMovieDetails: LiveData<MovieDetailsViewState>
        get() = _viewStateMovieDetails

    fun getMovieDetails(movieId: String) = viewModelScope.launch(dispatcher) {
        _viewStateMovieDetails.postValue(MovieDetailsViewState.Loading)

        when (val result = repository.getMovieDetails(movieId)) {
            is Result.Error -> _viewStateMovieDetails.postValue(MovieDetailsViewState.Error)
            is Result.Success -> {
                val details = detailsToDetailsViewStateMapper.invoke(result.data)
                details.isFavorite = isMovieInFavoritesUseCase.execute(result.data.id.toString())
                _viewStateMovieDetails.postValue(MovieDetailsViewState.ContentMovieDetails(
                    details
                ))
            }
        }
    }

    fun favoriteIconClicked(movie: DetailsViewState) {
        viewModelScope.launch(dispatcher) {
            addOrRemoveFromFavoritesUseCase.execute(movie)
            val currentViewState = _viewStateMovieDetails.value
            (currentViewState as? MovieDetailsViewState.ContentMovieDetails)?.let { content ->
                _viewStateMovieDetails.postValue(
                    content.updateFavoriteMovie(
                        movie,
                        isMovieInFavoritesUseCase.execute(movie.id.toString())
                    )
                )
            }
        }
    }
}