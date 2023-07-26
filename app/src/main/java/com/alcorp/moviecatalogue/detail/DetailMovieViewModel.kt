package com.alcorp.moviecatalogue.detail

import androidx.lifecycle.ViewModel
import com.alcorp.moviecatalogue.core.domain.model.Movie
import com.alcorp.moviecatalogue.core.domain.usecase.MovieUseCase

class DetailMovieViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    fun setFavoriteMovie(movie: Movie, newStatus:Boolean) =
        movieUseCase.setFavoriteMovie(movie, newStatus)
}