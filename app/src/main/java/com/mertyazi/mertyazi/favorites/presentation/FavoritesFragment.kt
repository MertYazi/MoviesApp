package com.mertyazi.mertyazi.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mertyazi.mertyazi.databinding.FragmentFavoritesBinding
import com.mertyazi.mertyazi.shared.Constants.POSTER_PATH
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var mFavoritesAdapter: FavoritesAdapter

    private var mAllFavoriteMovies = listOf<FavoriteMovieViewState>()
    private var mRandomMovie: FavoriteMovieViewState? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        initializeViewModel()

        binding.apply {
            ivFavoriteMoviesRandom.setOnClickListener {
                viewModel.getRandomMovie()
            }

            ivFavoriteMovies.setOnClickListener {
                mRandomMovie?.let { movie ->
                    findNavController().navigate(FavoritesFragmentDirections
                        .actionFavoritesFragmentToMovieDetailsFragment(
                            movie.id))
                }
            }
        }
    }

    private fun initializeViewModel() {
        viewModel.viewStateFavorites.observe(viewLifecycleOwner) { favoriteMovies ->
            when (favoriteMovies) {
                is FavoritesFragmentViewState.ContentFavorites -> {
                    binding.loader.visibility = View.GONE
                    mAllFavoriteMovies = favoriteMovies.favorites
                    mFavoritesAdapter.differ.submitList(favoriteMovies.favorites)
                }
                is FavoritesFragmentViewState.Error -> {
                    binding.loader.visibility = View.GONE
                }
                is FavoritesFragmentViewState.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                else -> { }
            }
        }

        viewModel.getFavoriteMovies()

        viewModel.viewStateRandom.observe(viewLifecycleOwner) { randomMovie ->
            when (randomMovie) {
                is FavoritesFragmentViewState.ContentRandom -> {
                    binding.loader.visibility = View.GONE
                    mRandomMovie = randomMovie.random
                    binding.ivFavoriteMovies.setImageDrawable(null)
                    mRandomMovie?.let { movie ->
                        Glide.with(this)
                            .load(POSTER_PATH + movie.moviePoster)
                            .into(binding.ivFavoriteMovies)
                    }
                }
                is FavoritesFragmentViewState.Error -> {
                    binding.loader.visibility = View.GONE
                }
                is FavoritesFragmentViewState.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                else -> { }
            }
        }
    }

    private fun setupRecyclerView() {
        mFavoritesAdapter = FavoritesAdapter(this)
        binding.rvFavoriteMovies.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvFavoriteMovies.adapter = mFavoritesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}