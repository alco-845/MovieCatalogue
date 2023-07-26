package com.alcorp.moviecatalogue.core.data.source.remote.network

import com.alcorp.moviecatalogue.core.BuildConfig
import com.alcorp.moviecatalogue.core.data.source.remote.response.ListMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val authToken: String = BuildConfig.API_KEY

interface ApiService {
    @GET("movie/popular?api_key=$authToken")
    suspend fun getMovieList(): ListMovieResponse

    @GET("search/movie?api_key=$authToken")
    suspend fun searchMovie(
        @Query("query") query: String
    ): ListMovieResponse
}