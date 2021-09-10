package com.example.movieapp.data.vo.PopularMovies


import com.google.gson.annotations.SerializedName

data class PopularMovies(
        @SerializedName("page")
    val page: Int,
        @SerializedName("results")
    val movieResults: List<MovieResult>,
        @SerializedName("total_pages")
    val totalPages: Int,
        @SerializedName("total_results")
    val totalResults: Int
)