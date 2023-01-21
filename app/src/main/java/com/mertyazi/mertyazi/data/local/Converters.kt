package com.mertyazi.mertyazi.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mertyazi.mertyazi.data.remote.responses.*

class Converters {

    @TypeConverter
    fun fromBelongsToCollection(belongsToCollection: BelongsToCollection?): String? {
        return Gson().toJson(belongsToCollection)
    }

    @TypeConverter
    fun toBelongsToCollection(belongsToCollectionString: String?): BelongsToCollection? {
        return Gson().fromJson(belongsToCollectionString, object : TypeToken<BelongsToCollection?>() {}.type)
    }

    @TypeConverter
    fun fromGenre(genre: List<Genre>): String {
        return Gson().toJson(genre)
    }

    @TypeConverter
    fun toGenre(genreString: String): List<Genre> {
        return Gson().fromJson(genreString, object : TypeToken<List<Genre>>() {}.type)
    }

    @TypeConverter
    fun fromProductionCompany(productionCompany: List<ProductionCompany>): String {
        return Gson().toJson(productionCompany)
    }

    @TypeConverter
    fun toProductionCompany(productionCompanyString: String): List<ProductionCompany> {
        return Gson().fromJson(productionCompanyString, object : TypeToken<List<ProductionCompany>>() {}.type)
    }

    @TypeConverter
    fun fromProductionCountry(productionCountry: List<ProductionCountry>): String {
        return Gson().toJson(productionCountry)
    }

    @TypeConverter
    fun toProductionCountry(productionCountryString: String): List<ProductionCountry> {
        return Gson().fromJson(productionCountryString, object : TypeToken<List<ProductionCountry>>() {}.type)
    }

    @TypeConverter
    fun fromSpokenLanguage(spokenLanguage: List<SpokenLanguage>): String {
        return Gson().toJson(spokenLanguage)
    }

    @TypeConverter
    fun toSpokenLanguage(spokenLanguageString: String): List<SpokenLanguage> {
        return Gson().fromJson(spokenLanguageString, object : TypeToken<List<SpokenLanguage>>() {}.type)
    }

}