package com.alcorp.moviecatalogue.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alcorp.moviecatalogue.core.domain.usecase.MovieUseCase

class MainViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    val movie = movieUseCase.getAllMovie().asLiveData()

    fun searchMovie(query: String) =
        movieUseCase.searchMovie(query).asLiveData()
}

