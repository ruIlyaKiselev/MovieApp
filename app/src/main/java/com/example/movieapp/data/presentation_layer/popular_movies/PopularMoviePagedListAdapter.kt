package com.example.movieapp.data.presentation_layer.popular_movies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.data.api.ApiContract
import com.example.movieapp.data.presentation_layer.single_movie_details.SingleMovie
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.PopularMovies.MovieResult
import com.example.movieapp.databinding.MovieListItemBinding
import com.example.movieapp.databinding.NetworkStateItemBinding

class PopularMoviePagedListAdapter(private val context: Context): PagedListAdapter<MovieResult, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState ?: NetworkState.ERROR)
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousNetworkState = this.networkState
        val hadExtraRow = hasExtraRow()

        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousNetworkState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) { 1 } else { 0 }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallback: DiffUtil.ItemCallback<MovieResult>() {
        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = MovieListItemBinding.bind(view)

        fun bind(movieResult: MovieResult?, context: Context) {
            binding.listItemMovieTitle.text = movieResult?.title
            binding.listItemReleaseDate.text = movieResult?.releaseDate

            val moviePosterUrl = ApiContract.POSTER_BASE_URL + movieResult?.posterPath
            Glide.with(itemView.context)
                    .load(moviePosterUrl)
                    .into(binding.listItemMoviePoster)

            itemView.setOnClickListener {
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movieResult?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = NetworkStateItemBinding.bind(view)

        fun bind(networkState: NetworkState) {
            if (networkState == NetworkState.LOADING) {
                binding.progressBarItem.visibility = View.VISIBLE
            } else {
                binding.progressBarItem.visibility = View.GONE
            }

            if (networkState == NetworkState.ERROR || networkState == NetworkState.ENDOFLIST) {
                binding.errorMsgItem.visibility = View.VISIBLE
                binding.errorMsgItem.text = networkState.message
            } else {
                binding.errorMsgItem.visibility = View.GONE
            }
        }
    }

    companion object {
        const val MOVIE_VIEW_TYPE = 1
        const val NETWORK_VIEW_TYPE = 2
    }
}