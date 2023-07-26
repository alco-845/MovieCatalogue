package com.alcorp.moviecatalogue.core.di

import androidx.room.Room
import com.alcorp.moviecatalogue.core.BuildConfig
import com.alcorp.moviecatalogue.core.data.MovieRepository
import com.alcorp.moviecatalogue.core.data.source.local.LocalDataSource
import com.alcorp.moviecatalogue.core.data.source.local.room.MovieDatabase
import com.alcorp.moviecatalogue.core.data.source.remote.RemoteDataSource
import com.alcorp.moviecatalogue.core.data.source.remote.network.ApiService
import com.alcorp.moviecatalogue.core.domain.repository.IMovieRepository
import com.alcorp.moviecatalogue.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val baseUrl: String = BuildConfig.BASE_URL

val databaseModule = module {
    factory { get<MovieDatabase>().movieDao() }
    single {
        val passphrase: ByteArray =  SQLiteDatabase.getBytes("alcorp".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java, "Movie.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = "api.themoviedb.org"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/NPIMWkzcNG/MyZsVExrC6tdy5LTZzeeKg2UlnGG55UY=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IMovieRepository> {
        MovieRepository(
            get(),
            get(),
            get()
        )
    }
}