package com.mertyazi.mertyazi.favorites.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mertyazi.mertyazi.databinding.ItemFavoriteBinding
import com.mertyazi.mertyazi.shared.Constants.POSTER_PATH

class FavoritesAdapter(private val fragment: Fragment): RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    class ViewHolder(view: ItemFavoriteBinding): RecyclerView.ViewHolder(view.root) {
        val favoriteImage = view.ivFavorite
    }

    private val differCallback = object: DiffUtil.ItemCallback<FavoriteMovieViewState>() {
        override fun areItemsTheSame(oldItem: FavoriteMovieViewState, newItem: FavoriteMovieViewState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteMovieViewState, newItem: FavoriteMovieViewState): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFavoriteBinding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteMovie = differ.currentList[position]
        holder.apply {
            Glide.with(favoriteImage)
                .load(POSTER_PATH + favoriteMovie.moviePosterWithTitle)
                .into(favoriteImage)

            itemView.setOnClickListener {
                fragment.findNavController().navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToMovieDetailsFragment(
                        favoriteMovie.id
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}