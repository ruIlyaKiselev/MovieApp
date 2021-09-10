package com.example.movieapp.data.presentation_layer.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.repository.MovieDetailsNetworkDataSource
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.MovieDetails.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService: TheMovieDbInterface) {
    var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource? = null

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource?.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource?.movieDetailsResponse ?: MutableLiveData<MovieDetails>() as LiveData<MovieDetails>
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource?.networkState ?: MutableLiveData<NetworkState>() as LiveData<NetworkState>
    }
}