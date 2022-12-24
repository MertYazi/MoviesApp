package com.mertyazi.mertyazi.data.remote.responses

import java.io.Serializable

data class SpokenLanguage(
    val english_name: String,
    val iso_639_1: String,
    val name: String
): Serializable