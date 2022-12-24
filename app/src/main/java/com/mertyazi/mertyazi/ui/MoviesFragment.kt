package com.mertyazi.mertyazi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mertyazi.mertyazi.adapters.NowPlayingAdapter
import com.mertyazi.mertyazi.adapters.UpcomingAdapter
import com.mertyazi.mertyazi.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import com.mertyazi.mertyazi.data.remote.responses.Result
import com.mertyazi.mertyazi.other.Constants.SLIDER_ITEM_COUNT
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations

@AndroidEntryPoint
class MoviesFragment : BaseFragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mNowPlayingAdapter: NowPlayingAdapter
    private lateinit var mUpcomingAdapter: UpcomingAdapter

    private var nowPlayingMoviesList: ArrayList<Result> = arrayListOf()
    private var subListOfNowPlayingMoviesList: ArrayList<Result> = arrayListOf()
    private var upcomingMoviesList: ArrayList<Result> = arrayListOf()

    private var isLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUpcomingAdapter()

        setupNowPlayingAdapter()

        initializeViewModel()

        loadMoreOnScroll()

        pullToRefresh()
    }

    private fun initializeViewModel() {
        viewModel.loader.observe(viewLifecycleOwner) { loading ->
            when (loading) {
                true -> binding.loader.visibility = View.VISIBLE
                else -> binding.loader.visibility = View.GONE
            }
        }

        viewModel.upcomingMovies.observe(viewLifecycleOwner) { upcomingMovies ->
            upcomingMoviesList = arrayListOf()
            upcomingMoviesList.addAll(upcomingMovies.results)
            mUpcomingAdapter.differ.submitList(upcomingMoviesList)
            val totalPages = upcomingMovies.total_pages
            isLastPage = viewModel.upcomingMoviesPage == totalPages
        }

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) { nowPlayingMovies ->
            nowPlayingMoviesList = arrayListOf()
            subListOfNowPlayingMoviesList = arrayListOf()
            nowPlayingMoviesList.addAll(nowPlayingMovies.results)
            subListOfNowPlayingMoviesList.addAll(nowPlayingMoviesList.subList(0, SLIDER_ITEM_COUNT))
            mNowPlayingAdapter.submitList(subListOfNowPlayingMoviesList)
        }
    }

    private fun setupNowPlayingAdapter() {
        mNowPlayingAdapter = NowPlayingAdapter(this)
        mNowPlayingAdapter.submitList(subListOfNowPlayingMoviesList)
        binding.svNowPlaying.apply {
            setSliderAdapter(mNowPlayingAdapter)
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            startAutoCycle()
        }
    }

    private fun setupUpcomingAdapter() {
        mUpcomingAdapter = UpcomingAdapter(this)
        binding.rvUpcoming.apply {
            adapter = mUpcomingAdapter
            layoutManager = LinearLayoutManager(activity)
            isNestedScrollingEnabled = false
        }
    }

    private fun loadMoreOnScroll() {
        binding.svMovies.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            val lastChild = binding.svMovies.getChildAt(binding.svMovies.childCount - 1)
            if (lastChild != null) {
                if ((scrollY >= (lastChild.measuredHeight - v.measuredHeight)) &&
                    scrollY > oldScrollY && !isLastPage) {
                    viewModel.getUpcomingMovies()
                }
            }
        })
    }

    private fun pullToRefresh() {
        binding.srlMovies.apply {
            setOnRefreshListener {
                viewModel.refreshUpcomingMovies()
                viewModel.refreshNowPlayingMovies()
                isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}