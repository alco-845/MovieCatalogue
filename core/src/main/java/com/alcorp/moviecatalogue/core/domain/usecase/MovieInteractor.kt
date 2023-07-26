package com.alcorp.moviecatalogue.core.domain.usecase

import com.alcorp.moviecatalogue.core.domain.model.Movie
import com.alcorp.moviecatalogue.core.domain.repository.IMovieRepository

class MovieInteractor(private val movieRepository: IMovieRepository): MovieUseCase {
    override fun getAllMovie() = movieRepository.getAllMovie()
    override fun searchMovie(query: String) = movieRepository.searchMovie(query)
    override fun getFavoriteMovie() = movieRepository.getFavoriteMovie()
    override fun setFavoriteMovie(movie: Movie, state: Boolean) = movieRepository.setFavoriteMovie(movie, state)
}