package com.example.movieapp.data.api

import com.example.movieapp.data.vo.MovieDetails.MovieDetails
import com.example.movieapp.data.vo.PopularMovies.PopularMovies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbInterface {
//    https://api.themoviedb.org/3/movie/popular?api_key=87ee1f125f14b6150bcc81ea5bf91f1b
//    https://api.themoviedb.org/3/movie/588228?api_key=87ee1f125f14b6150bcc81ea5bf91f1b
//    https://api.themoviedb.org/3/

    @GET("movie/{movieId}")
    fun getMovieDetails(@Path("movieId") movieId: Int): Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<PopularMovies>
}