package com.mertyazi.mertyazi.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mertyazi.mertyazi.R
import com.mertyazi.mertyazi.data.remote.responses.MovieDetailsResponse
import com.mertyazi.mertyazi.databinding.FragmentMovieDetailsBinding
import com.mertyazi.mertyazi.other.Constants.BASE_IMDB
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMovieDetails: MovieDetailsResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViewModel()

        binding.apply {
            clImdbContent.setOnClickListener {
                redirectToImdbApp()
            }
        }
    }

    private fun populateUI() {
        binding.apply {
            movieDetails = mMovieDetails
        }
    }

    private fun initializeViewModel() {
        val args: MovieDetailsFragmentArgs by navArgs()

        viewModel.movieId = args.movieId

        viewModel.loader.observe(viewLifecycleOwner) { loading ->
            when (loading) {
                true -> binding.loader.visibility = View.VISIBLE
                else -> binding.loader.visibility = View.GONE
            }
        }

        viewModel.movieDetails.observe(viewLifecycleOwner) { movieDetails ->
            if (movieDetails.getOrNull() != null) {
                mMovieDetails = movieDetails.getOrNull()!!.body()!!
                populateUI()
            } else {
                Log.e("apiDataMovieDetails", "is null")
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.details_not_ready),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack(R.id.moviesFragment, false)
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun redirectToImdbApp() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(BASE_IMDB + mMovieDetails.imdb_id)
        )
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}