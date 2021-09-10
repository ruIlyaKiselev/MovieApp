package com.example.movieapp.data.presentation_layer.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movieapp.data.api.ApiContract
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.repository.MovieDataSource
import com.example.movieapp.data.repository.MovieDataSourceFactory
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.PopularMovies.MovieResult
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService: TheMovieDbInterface) {
    var moviePagedList: LiveData<PagedList<MovieResult>>? = null
    var moviesDataSourceFactory: MovieDataSourceFactory? = null

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<MovieResult>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(ApiContract.POST_PER_PAGE)
                .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory!!, config).build()

        return moviePagedList!!
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
                moviesDataSourceFactory!!.movieLiveDataSource,
                MovieDataSource::networkState
        )
    }
}