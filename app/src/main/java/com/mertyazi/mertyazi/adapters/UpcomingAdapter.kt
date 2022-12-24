package com.mertyazi.mertyazi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mertyazi.mertyazi.databinding.ItemUpcomingBinding
import com.mertyazi.mertyazi.data.remote.responses.Result
import com.mertyazi.mertyazi.other.Constants.POSTER_PATH
import com.mertyazi.mertyazi.other.Constants.convertDate
import com.mertyazi.mertyazi.other.Constants.minifyDate
import com.mertyazi.mertyazi.ui.MoviesFragmentDirections

class UpcomingAdapter(private val fragment: Fragment): RecyclerView.Adapter<UpcomingAdapter.ViewHolder>() {

    class ViewHolder(view: ItemUpcomingBinding): RecyclerView.ViewHolder(view.root) {
        val movieImage = view.ivMovie
        val movieTitle = view.tvMovieTitle
        val movieDescription = view.tvMovieDescription
        val movieDate = view.tvMovieDate
    }

    private val differCallback = object: DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemUpcomingBinding = ItemUpcomingBinding.inflate(
            LayoutInflater.from(fragment.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val upcoming = differ.currentList[position]
        holder.apply {
            Glide.with(fragment)
                .load(POSTER_PATH + upcoming.backdrop_path)
                .into(movieImage)
            movieTitle.text = upcoming.title + minifyDate(upcoming.release_date)
            movieDescription.text = upcoming.overview
            movieDate.text = convertDate(upcoming.release_date)

            itemView.setOnClickListener {
                fragment.findNavController().navigate(
                    MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(
                        upcoming.id.toString()
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}