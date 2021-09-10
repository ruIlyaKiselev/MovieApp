package com.example.movieapp.data.presentation_layer.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.movieapp.data.api.ApiContract
import com.example.movieapp.data.api.TheMovieDbClient
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.MovieDetails.MovieDetails
import com.example.movieapp.databinding.ActivitySingleMovieBinding

class SingleMovie : AppCompatActivity() {

    private lateinit var binding: ActivitySingleMovieBinding

    private var viewModel: SingleMovieViewModel? = null
    private var movieRepository: MovieDetailsRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val movieId: Int = intent.getIntExtra("id", 0)

        val apiService: TheMovieDbInterface = TheMovieDbClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel?.movieDetails?.observe(this) {
            bindUI(it)
        }

        viewModel?.networkState?.observe(this) {

            binding.progressBar.visibility = if (it == NetworkState.LOADING) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.txtError.visibility = if (it == NetworkState.ERROR) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun bindUI(it: MovieDetails?) {
        binding.movieTitle.text = it?.title
        binding.movieTagline.text = it?.tagline
        binding.movieReleaseDateNumber.text = it?.releaseDate
        binding.movieRatingNumber.text = it?.voteAverage.toString()
        binding.movieRuntimeNumber.text = it?.runtime.toString() + " minutes"
        binding.movieOverviewContent.text = it?.overview
        binding.movieBudgetNumber.text = it?.budget.toString() + "$"
        binding.movieRevenueNumber.text = it?.revenue.toString() + "$"

        val moviePosterURL = ApiContract.POSTER_BASE_URL + it?.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(binding.poster)
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel{
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED CAST")
                return SingleMovieViewModel(movieRepository!!, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}