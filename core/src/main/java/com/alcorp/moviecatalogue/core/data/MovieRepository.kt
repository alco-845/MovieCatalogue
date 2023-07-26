package com.alcorp.moviecatalogue.core.data

import com.alcorp.moviecatalogue.core.data.source.local.LocalDataSource
import com.alcorp.moviecatalogue.core.data.source.remote.RemoteDataSource
import com.alcorp.moviecatalogue.core.data.source.remote.network.ApiResponse
import com.alcorp.moviecatalogue.core.data.source.remote.response.MovieResponse
import com.alcorp.moviecatalogue.core.domain.model.Movie
import com.alcorp.moviecatalogue.core.domain.repository.IMovieRepository
import com.alcorp.moviecatalogue.core.utils.AppExecutors
import com.alcorp.moviecatalogue.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IMovieRepository {

    override fun getAllMovie(): Flow<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<Movie>, List<MovieResponse>>(appExecutors) {
            override fun loadFromDB(): Flow<List<Movie>> {
                return localDataSource.getAllMovie().map { DataMapper.mapEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                true

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.getAllMovie()

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertMovie(movieList)
            }
        }.asFlow()

    override fun searchMovie(query: String): Flow<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<Movie>, List<MovieResponse>>(appExecutors) {
            override fun loadFromDB(): Flow<List<Movie>> {
                return localDataSource.searchMovie(query).map { DataMapper.mapEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                true

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.searchMovie(query)

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertMovie(movieList)
            }
        }.asFlow()

    override fun getFavoriteMovie(): Flow<List<Movie>> {
        return localDataSource.getFavoriteMovie().map { DataMapper.mapEntitiesToDomain(it) }
    }

    override fun setFavoriteMovie(movie: Movie, state: Boolean) {
        val movieEntity = DataMapper.mapDomainToEntity(movie)
        appExecutors.diskIO().execute { localDataSource.setFavoriteMovie(movieEntity, state) }
    }
}

