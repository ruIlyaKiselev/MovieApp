package com.example.movieapp.data.repository

class NetworkState (val status: NetworkStatus, val message: String) {

    companion object {
        val LOADED: NetworkState = NetworkState(NetworkStatus.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(NetworkStatus.RUNNING, "Running")
        val ERROR: NetworkState = NetworkState(NetworkStatus.FAILED, "Failed")
        val ENDOFLIST: NetworkState = NetworkState(NetworkStatus.FAILED, "Reached the end")
    }
}