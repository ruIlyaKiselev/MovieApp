package com.example.movieapp.data.presentation_layer.popular_movies

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.data.api.TheMovieDbClient
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var mainActivityViewModel: MainActivityViewModel? = null
    var moviePagedListRepository: MoviePagedListRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val apiService: TheMovieDbInterface = TheMovieDbClient.getClient()

        moviePagedListRepository = MoviePagedListRepository(apiService)

        mainActivityViewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemViewType = movieAdapter.getItemViewType(position)

                return if (itemViewType == PopularMoviePagedListAdapter.MOVIE_VIEW_TYPE) {
                    1
                } else {
                    3
                }
            }
        }

        binding.rcMovieList.layoutManager = gridLayoutManager
        binding.rcMovieList.setHasFixedSize(true)
        binding.rcMovieList.adapter = movieAdapter

        mainActivityViewModel?.moviePagedList?.observe(this) {
            movieAdapter.submitList(it)
        }

        mainActivityViewModel?.networkState?.observe(this) {
            binding.progressBarPopular.visibility =
                    if (mainActivityViewModel!!.listIsEmpty() && it == NetworkState.LOADING) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

            binding.txtErrorPopular.visibility =
                    if (mainActivityViewModel!!.listIsEmpty() && it == NetworkState.ERROR) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }

            if (mainActivityViewModel?.listIsEmpty() == false) {
                movieAdapter.setNetworkState(it)
            }
        }
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED CAST")
                return MainActivityViewModel(moviePagedListRepository!!) as T
            }
        })[MainActivityViewModel::class.java]
    }
}