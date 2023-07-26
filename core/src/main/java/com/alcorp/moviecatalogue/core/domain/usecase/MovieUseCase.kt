package com.alcorp.moviecatalogue.core.domain.usecase

import com.alcorp.moviecatalogue.core.data.Resource
import com.alcorp.moviecatalogue.core.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getAllMovie(): Flow<Resource<List<Movie>>>
    fun searchMovie(query: String): Flow<Resource<List<Movie>>>
    fun getFavoriteMovie(): Flow<List<Movie>>
    fun setFavoriteMovie(movie: Movie, state: Boolean)
}