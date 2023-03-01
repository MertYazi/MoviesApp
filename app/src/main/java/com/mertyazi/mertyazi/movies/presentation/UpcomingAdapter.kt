package com.mertyazi.mertyazi.movies.presentation

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

class UpcomingAdapter(private val fragment: Fragment): RecyclerView.Adapter<UpcomingAdapter.ViewHolder>() {

    class ViewHolder(view: ItemUpcomingBinding): RecyclerView.ViewHolder(view.root) {
        val movieImage = view.ivMovie
        val movieTitle = view.tvMovieTitle
        val movieDescription = view.tvMovieDescription
        val movieDate = view.tvMovieDate
    }

    private val differCallback = object: DiffUtil.ItemCallback<ResultViewState>() {
        override fun areItemsTheSame(oldItem: ResultViewState, newItem: ResultViewState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResultViewState, newItem: ResultViewState): Boolean {
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
                .load(upcoming.backdrop_path)
                .into(movieImage)
            movieTitle.text = upcoming.title
            movieDescription.text = upcoming.overview
            movieDate.text = upcoming.release_date

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