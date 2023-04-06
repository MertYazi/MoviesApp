package com.mertyazi.mertyazi.favorites.business.mapper

import com.mertyazi.mertyazi.details.presentation.DetailsViewState
import com.mertyazi.mertyazi.favorites.data.repository.database.FavoriteMovieEntity
import javax.inject.Inject

class DetailsViewStateToFavoriteMapper @Inject constructor(
): Function1<DetailsViewState, FavoriteMovieEntity> {

    override fun invoke(detailsViewState: DetailsViewState): FavoriteMovieEntity {
        return FavoriteMovieEntity(
            detailsViewState.id.toString(),
            detailsViewState.title ?: "",
            detailsViewState.backdrop_path ?: "",
            detailsViewState.poster_path ?: ""
        )
    }
}