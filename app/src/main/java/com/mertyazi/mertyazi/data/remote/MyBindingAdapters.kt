package com.mertyazi.mertyazi.data.remote

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mertyazi.mertyazi.data.remote.responses.Genre
import com.mertyazi.mertyazi.other.Constants.POSTER_PATH
import com.mertyazi.mertyazi.other.Constants.convertDate
import com.mertyazi.mertyazi.other.Constants.minifyDate
import java.math.BigDecimal
import java.math.RoundingMode

@BindingAdapter("convertVoteAverageToString")
fun TextView.convertVoteAverageToString(value: Double?) {
    if (value != null) {
        this.text =  BigDecimal(value)
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
            .toString()
    } else {
        this.text =  ""
    }
}

@BindingAdapter("convertReleaseDateToString")
fun TextView.convertReleaseDateToString(value: String?) {
    if (value != null) {
        this.text =  convertDate(value)
    } else {
        this.text =  ""
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("convertTitleToString", "minifyDate")
fun TextView.convertTitleToString(value: String?, date: String?) {
    if (value != null && date != null) {
        this.text =  value + minifyDate(date)
    } else {
        this.text =  ""
    }
}

@BindingAdapter("convertGenresToString")
fun TextView.convertGenresToString(genres: List<Genre>?) {
    if (genres != null) {
        val genresList = StringBuilder(256)
        for (genre in genres) {
            genresList.append(" " + genre.name + "  ")
        }
        this.text = genresList.toString()
    } else {
        this.text =  ""
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setMaxRate")
fun TextView.setMaxRate(value: Int?) {
    this.text = "/" + value.toString()
}

@BindingAdapter("setImageView")
fun ImageView.setImage(value: String?) {
    if (value != null) {
        Glide.with(this)
            .load(POSTER_PATH + value)
            .into(this)
    }
}
