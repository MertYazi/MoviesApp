package com.mertyazi.mertyazi.details.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mertyazi.mertyazi.databinding.FragmentMovieDetailsBinding
import com.mertyazi.mertyazi.shared.Constants.BASE_IMDB
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()

    private lateinit var mMovieId: String
    private lateinit var mMovieDetails: DetailsViewState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: MovieDetailsFragmentArgs by navArgs()
        mMovieId = args.movieId

        initializeViewModel()

        binding.apply {
            clImdbContent.setOnClickListener {
                redirectToImdbApp()
            }
            ivFavoriteMovie.setOnClickListener {
                viewModel.favoriteIconClicked(mMovieDetails)
            }
        }
    }

    private fun populateUI() {
        binding.apply {
            movieDetails = mMovieDetails
            ivFavoriteMovie.visibility = View.VISIBLE
        }
    }

    private fun initializeViewModel() {
        viewModel.viewStateMovieDetails.observe(viewLifecycleOwner) { movieDetails ->
            when (movieDetails) {
                is MovieDetailsViewState.ContentMovieDetails -> {
                    binding.loader.visibility = View.GONE
                    mMovieDetails = movieDetails.details
                    populateUI()
                }
                is MovieDetailsViewState.Error -> {
                    binding.loader.visibility = View.GONE
                }
                is MovieDetailsViewState.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
            }
        }

        viewModel.getMovieDetails(mMovieId)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun redirectToImdbApp() {
        if (mMovieDetails.imdb_id!!.isNotEmpty()) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(BASE_IMDB + mMovieDetails.imdb_id)
            )
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}