package com.alcorp.moviecatalogue.di

import com.alcorp.moviecatalogue.core.domain.usecase.MovieInteractor
import com.alcorp.moviecatalogue.core.domain.usecase.MovieUseCase
import com.alcorp.moviecatalogue.detail.DetailMovieViewModel
import com.alcorp.moviecatalogue.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailMovieViewModel(get()) }
}
