package com.mertyazi.mertyazi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mertyazi.mertyazi.adapters.FavoritesAdapter
import com.mertyazi.mertyazi.data.remote.responses.MovieDetailsResponse
import com.mertyazi.mertyazi.databinding.FragmentFavoritesBinding
import com.mertyazi.mertyazi.other.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mFavoritesAdapter: FavoritesAdapter

    private var mAllFavoriteMovies = listOf<MovieDetailsResponse>()
    private var mRandomMovie: MovieDetailsResponse? = null

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
                            movie.id.toString()))
                }
            }
        }

    }

    private fun initializeViewModel() {
        viewModel.loader.observe(viewLifecycleOwner) { loading ->
            when (loading) {
                true -> binding.loader.visibility = View.VISIBLE
                else -> binding.loader.visibility = View.GONE
            }
        }

        viewModel.getFavoriteMovies().observe(viewLifecycleOwner) { movies ->
            mAllFavoriteMovies = movies
            mFavoritesAdapter.differ.submitList(movies)
        }

        viewModel.randomMovie.observe(viewLifecycleOwner) { randomMovie ->
            mRandomMovie = randomMovie
            binding.ivFavoriteMovies.setImageDrawable(null)
            mRandomMovie?.let { movie ->
                Glide.with(this)
                    .load(Constants.POSTER_PATH + movie.backdrop_path)
                    .into(binding.ivFavoriteMovies)
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