package com.mertyazi.mertyazi.details.presentation

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mertyazi.mertyazi.R
import com.mertyazi.mertyazi.shared.Constants.POSTER_PATH
import com.mertyazi.mertyazi.details.business.entities.Genre

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
fun ImageView.setImageView(value: String?) {
    if (value != null) {
        Glide.with(this)
            .load(POSTER_PATH + value)
            .into(this)
    }
}

@BindingAdapter("setFavorite")
fun ImageView.setFavorite(value: Boolean) {
    if (value) {
        Glide.with(this)
            .load(R.drawable.ic_favorite_selected)
            .into(this)
    } else {
        Glide.with(this)
            .load(R.drawable.ic_favorite_unselected)
            .into(this)
    }
}
