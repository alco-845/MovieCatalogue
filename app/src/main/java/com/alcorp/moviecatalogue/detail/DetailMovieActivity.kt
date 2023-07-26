package com.alcorp.moviecatalogue.detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alcorp.moviecatalogue.R
import com.alcorp.moviecatalogue.core.domain.model.Movie
import com.alcorp.moviecatalogue.databinding.ActivityDetailMovieBinding
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailMovieActivity : AppCompatActivity() {

    private val detailMovieViewModel: DetailMovieViewModel by viewModel()
    private lateinit var binding: ActivityDetailMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabBack.setOnClickListener { finish() }

        val detailMovie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DATA, Movie::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_DATA)
        }

        loadData(detailMovie)
    }

    private fun loadData(detailMovie: Movie?) {
        detailMovie?.let {
            binding.tvDetailMovieTitle.text = detailMovie.title
            binding.tvDetailMovieOverview.text = detailMovie.overview
            Glide.with(this@DetailMovieActivity)
                .load(resources.getString(R.string.image_url) + detailMovie.posterPath)
                .into(binding.ivDetailMovie)

            binding.tvDetailMovieReleaseDate.text = detailMovie.releaseDate
            binding.tvDetailMovieRating.text = detailMovie.voteAverage

            var statusFavorite = detailMovie.isFavorite
            setStatusFavorite(statusFavorite)
            binding.fabFavorite.setOnClickListener {
                statusFavorite = !statusFavorite
                detailMovieViewModel.setFavoriteMovie(detailMovie, statusFavorite)
                setStatusFavorite(statusFavorite)
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24))
        } else {
            binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24))
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}