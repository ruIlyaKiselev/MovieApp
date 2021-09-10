package com.example.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movieapp.data.api.ApiContract
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.vo.PopularMovies.MovieResult
import com.example.movieapp.data.vo.PopularMovies.PopularMovies
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(
        private val apiService: TheMovieDbInterface,
        private val compositeDisposable: CompositeDisposable
        ): PageKeyedDataSource<Int, MovieResult>() {

    private val page = ApiContract.FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieResult>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
                apiService.getPopularMovies(page)
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            callback.onResult(it.movieResults, null, page + 1)
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                            Log.e("MyTag", it.message ?: "")
                        })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
                apiService.getPopularMovies(params.key)
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            if (it.totalPages >= params.key) {
                                callback.onResult(it.movieResults, params.key + 1)
                                networkState.postValue(NetworkState.LOADED)
                            } else {
                                networkState.postValue(NetworkState.ENDOFLIST)
                            }
                        }, {
                            networkState.postValue(NetworkState.ERROR)
                            Log.e("MyTag", it.message ?: "")
                        })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieResult>) {

    }
}