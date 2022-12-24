package com.mertyazi.mertyazi.other

object Constants {

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "api_key"
    const val MOVIE_ID = "movie_id"
    const val POSTER_PATH = "https://image.tmdb.org/t/p/w500"
    const val SLIDER_ITEM_COUNT = 5
    const val BASE_IMDB = "imdb:///title/"
    const val PAGE_SIZE = 20
    const val MAX_RATE = 10
    const val THE_WOMAN_KING_TITLE = "The Woman King (2022)"
    const val THE_WOMAN_KING_DESCRIPTION = "The story of the Agojie, the all-female unit of warriors who protected the African Kingdom of Dahomey in the 1800s with skills and a fierceness unlike anything the world has ever seen, and General Nanisca as she trains the next generation of recruits and readies them for battle against an enemy determined to destroy their way of life."
    const val THE_WOMAN_KING_DATE = "15.09.2022"

    fun convertDate(date: String): String {
        val dateArray: List<String> = date.split("-")

        return dateArray[2] + "." + dateArray[1] + "." + dateArray[0]
    }

    fun minifyDate(fullDate: String): String {
        val dateArray: List<String> = fullDate.split("-")

        return " (" + dateArray[0] + ")"
    }
}