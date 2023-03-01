package com.mertyazi.mertyazi.movies.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mertyazi.mertyazi.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import com.mertyazi.mertyazi.shared.Constants.PAGE_SIZE
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()

    private lateinit var mNowPlayingAdapter: NowPlayingAdapter
    private lateinit var mUpcomingAdapter: UpcomingAdapter

    private var nowPlayingMoviesList: ArrayList<ResultViewState> = arrayListOf()
    private var upcomingMoviesList: ArrayList<ResultViewState> = arrayListOf()

    private var isLastPage = false
    private var isScrolling = false

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

        detectLandscape()

        pullToRefresh()
    }

    private fun initializeViewModel() {
        viewModel.viewStateUpcoming.observe(viewLifecycleOwner) { upcomingMovies ->
            when (upcomingMovies) {
                is MoviesViewState.ContentUpcoming -> {
                    binding.loader.visibility = View.GONE
                    upcomingMoviesList = arrayListOf()
                    upcomingMoviesList.addAll(upcomingMovies.upcoming.results)
                    mUpcomingAdapter.differ.submitList(upcomingMoviesList)
                    val totalPages = upcomingMovies.upcoming.totalPages
                    isLastPage = viewModel.upcomingMoviesPage == totalPages
                }
                is MoviesViewState.Error -> {
                    binding.loader.visibility = View.GONE
                }
                is MoviesViewState.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                else -> { }
            }
        }

        viewModel.viewStateNowPlaying.observe(viewLifecycleOwner) { nowPlayingMovies ->
            when (nowPlayingMovies) {
                is MoviesViewState.ContentNowPlaying -> {
                    binding.loader.visibility = View.GONE
                    nowPlayingMoviesList = ArrayList(nowPlayingMovies.nowPlaying.results)
                    mNowPlayingAdapter.submitList(nowPlayingMoviesList)
                }
                is MoviesViewState.Error -> {
                    binding.loader.visibility = View.GONE
                }
                is MoviesViewState.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                else -> { }
            }
        }
    }

    private fun setupNowPlayingAdapter() {
        mNowPlayingAdapter = NowPlayingAdapter(this)
        mNowPlayingAdapter.submitList(nowPlayingMoviesList)
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

    private fun loadMoreOnScrollView() {
        binding.svMovies.let {
            binding.svMovies!!.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                    val lastChild = binding.svMovies!!.getChildAt(binding.svMovies!!.childCount - 1)
                    if (lastChild != null) {
                        if ((scrollY >= (lastChild.measuredHeight - v.measuredHeight)) &&
                            scrollY > oldScrollY && !isLastPage) {
                            viewModel.getUpcomingMovies()
                        }
                    }
                })
        }
    }

    private val scrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage &&
                    isAtLastItem &&
                    isNotAtBeginning &&
                    isTotalMoreThanVisible &&
                    isScrolling
            if (shouldPaginate) {
                viewModel.getUpcomingMovies()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
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

    private fun detectLandscape() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUpcoming.apply {
                isNestedScrollingEnabled = true
                addOnScrollListener(this@MoviesFragment.scrollListener)
            }
        } else {
            loadMoreOnScrollView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}