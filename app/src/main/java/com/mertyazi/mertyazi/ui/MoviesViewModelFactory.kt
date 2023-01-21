package com.mertyazi.mertyazi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mertyazi.mertyazi.repositories.MoviesRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory @Inject constructor(
    private val repository: MoviesRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MoviesViewModel(repository) as T
    }
}
