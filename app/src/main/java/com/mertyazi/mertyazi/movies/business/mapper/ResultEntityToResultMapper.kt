package com.mertyazi.mertyazi.movies.business.mapper

import com.mertyazi.mertyazi.movies.data.entities.ResultEntity
import javax.inject.Inject

class ResultEntityToResultMapper @Inject constructor(
): Function1<ResultEntity, com.mertyazi.mertyazi.movies.business.entities.Result> {

    override fun invoke(resultEntity: ResultEntity): com.mertyazi.mertyazi.movies.business.entities.Result {
        return com.mertyazi.mertyazi.movies.business.entities.Result(
            resultEntity.backdrop_path ?: "",
            resultEntity.genre_ids ?: listOf(),
            resultEntity.id,
            resultEntity.overview ?: "",
            resultEntity.poster_path ?: "",
            resultEntity.release_date ?: "",
            resultEntity.title ?: "",
            resultEntity.vote_average ?: 0.0
        )
    }
}