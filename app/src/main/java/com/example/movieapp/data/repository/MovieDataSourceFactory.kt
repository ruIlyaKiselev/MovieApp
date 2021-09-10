package com.example.movieapp.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movieapp.data.api.TheMovieDbInterface
import com.example.movieapp.data.vo.PopularMovies.MovieResult
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
        private val apiService: TheMovieDbInterface,
        private val compositeDisposable: CompositeDisposable
        ): DataSource.Factory<Int, MovieResult>() {

    val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, MovieResult> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)
        movieLiveDataSource.postValue(movieDataSource)

        return movieDataSource
    }
}