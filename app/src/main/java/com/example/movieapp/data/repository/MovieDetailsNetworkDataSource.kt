package com.example.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.vo.MovieDetails.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource (private val apiService: TheMovieDbInterface, private val compositeDisposable: CompositeDisposable) {
    private val mutableNetworkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = mutableNetworkState

    private val mutableMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val movieDetailsResponse: LiveData<MovieDetails>
        get() = mutableMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {
        mutableNetworkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        mutableMovieDetailsResponse.postValue(it)
                        mutableNetworkState.postValue(NetworkState.LOADED)
                    }, {
                        mutableNetworkState.postValue(NetworkState.ERROR)
                        Log.e("MyLog", it.message ?: "")
                    })
            )
        } catch (e: Exception) {
//            mutableNetworkState.postValue(NetworkState.ERROR)
            Log.e("MyLog", e.message ?: "")
        }
    }
}