package com.mertyazi.mertyazi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.mertyazi.mertyazi.data.remote.responses.Result
import com.mertyazi.mertyazi.databinding.ItemNowPlayingBinding
import com.mertyazi.mertyazi.other.Constants.POSTER_PATH
import com.mertyazi.mertyazi.other.Constants.minifyDate
import com.mertyazi.mertyazi.ui.MoviesFragmentDirections

class NowPlayingAdapter(private val fragment: Fragment): SliderViewAdapter<NowPlayingAdapter.ViewHolder>() {

    private var nowPlayingMovies: ArrayList<Result> = arrayListOf()

    class ViewHolder(view: ItemNowPlayingBinding): SliderViewAdapter.ViewHolder(view.root) {
        val ivNowPlaying = view.ivNowPlaying
        val tvNowPlayingTitle = view.tvNowPlayingTitle
        val tvNowPlayingDescription = view.tvNowPlayingDescription
    }

    override fun getCount(): Int {
        return nowPlayingMovies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val binding: ItemNowPlayingBinding = ItemNowPlayingBinding.inflate(
            LayoutInflater.from(fragment.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        val nowPlaying = nowPlayingMovies[position]
        Glide.with(fragment)
            .load(POSTER_PATH + nowPlaying.backdrop_path)
            .into(viewHolder!!.ivNowPlaying)
        viewHolder.tvNowPlayingTitle.text = nowPlaying.title + minifyDate(nowPlaying.release_date)
        viewHolder.tvNowPlayingDescription.text = nowPlaying.overview
        viewHolder.ivNowPlaying.setOnClickListener {
            fragment.findNavController().navigate(
                MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(
                    nowPlaying.id.toString()
                )
            )
        }
    }

    fun submitList(list: ArrayList<Result>) {
        nowPlayingMovies = arrayListOf()
        nowPlayingMovies.addAll(list)
        notifyDataSetChanged()
    }

}