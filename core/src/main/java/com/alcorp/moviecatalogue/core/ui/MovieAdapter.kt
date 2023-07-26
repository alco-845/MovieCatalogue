package com.alcorp.moviecatalogue.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alcorp.moviecatalogue.core.R
import com.alcorp.moviecatalogue.core.databinding.ItemMovieBinding
import com.alcorp.moviecatalogue.core.domain.model.Movie
import com.alcorp.moviecatalogue.core.utils.MovieDiffUtilCallback
import com.bumptech.glide.Glide

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ListViewHolder>() {

    private var listData = ArrayList<Movie>()
    var onItemClick: ((Movie) -> Unit)? = null

    fun setData(newListData: List<Movie>?) {
        if (newListData == null) return
        val diffResult = DiffUtil.calculateDiff(MovieDiffUtilCallback(listData, newListData))
        listData.clear()
        listData.addAll(newListData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        with(holder) {
            Glide.with(itemView.context)
                .load(itemView.context.resources.getString(R.string.image_url) + data.posterPath)
                .into(binding.ivMovie)

            binding.tvMovieTitle.text = data.title
            binding.tvMovieReleaseDate.text = data.releaseDate
            binding.tvMovieRating.text = data.voteAverage

            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }

    inner class ListViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)
}