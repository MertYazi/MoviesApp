package com.mertyazi.mertyazi.data.remote.responses

import java.io.Serializable

data class ProductionCompany(
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
):Serializable