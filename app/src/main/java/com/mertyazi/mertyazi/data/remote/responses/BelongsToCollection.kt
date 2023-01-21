package com.mertyazi.mertyazi.data.remote.responses

import java.io.Serializable

data class BelongsToCollection(
    val backdrop_path: String?,
    val id: Int?,
    val name: String?,
    val poster_path: String?
): Serializable