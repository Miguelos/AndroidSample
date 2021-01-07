package me.miguelos.sample.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import me.miguelos.sample.BuildConfig
import me.miguelos.sample.common.Mapper
import me.miguelos.sample.common.TwoWaysMapper
import me.miguelos.sample.data.MarvelRepositoryImpl
import me.miguelos.sample.data.source.local.MarvelCharacterDao
import me.miguelos.sample.data.source.local.MarvelDatabase
import me.miguelos.sample.data.source.local.MarvelLocalRemoteDataSource
import me.miguelos.sample.data.source.local.entity.MarvelCharacterDBEntity
import me.miguelos.sample.data.source.local.entity.mapper.MarvelCharacterDBMapper
import me.miguelos.sample.data.source.remote.MarvelRemoteRemoteDataSource
import me.miguelos.sample.data.source.remote.api.AuthInterceptor
import me.miguelos.sample.data.source.remote.api.MarvelService
import me.miguelos.sample.data.source.remote.entity.ImageEntity
import me.miguelos.sample.data.source.remote.entity.MarvelCharacterEntity
import me.miguelos.sample.data.source.remote.entity.UrlEntity
import me.miguelos.sample.data.source.remote.entity.mapper.ImageMapper
import me.miguelos.sample.data.source.remote.entity.mapper.MarvelCharacterMapper
import me.miguelos.sample.data.source.remote.entity.mapper.UrlMapper
import me.miguelos.sample.domain.MarvelRepository
import me.miguelos.sample.domain.model.Image
import me.miguelos.sample.domain.model.Url
import me.miguelos.sample.util.Timeouts
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import me.miguelos.sample.domain.model.MarvelCharacter as DomainMarvelCharacter


@Module
@InstallIn(ApplicationComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideMarvelRepository(
        marvelLocalDataSource: MarvelLocalRemoteDataSource,
        marvelRemoteDataSource: MarvelRemoteRemoteDataSource
    ): MarvelRepository =
        MarvelRepositoryImpl(marvelLocalDataSource, marvelRemoteDataSource)

    @Provides
    @Singleton
    internal fun provideMarvelService(
        retrofit: Retrofit
    ): MarvelService =
        retrofit.create(MarvelService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Timeouts.CONNECT, TimeUnit.SECONDS)
            .readTimeout(Timeouts.READ, TimeUnit.SECONDS)
            .writeTimeout(Timeouts.WRITE, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHttp").d(message)
            }
        }).apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideAuthInterceptor() = AuthInterceptor(
        BuildConfig.API_PUBLIC_KEY,
        BuildConfig.API_PRIVATE_KEY
    )

    @Provides
    @Singleton
    fun providesMarvelDao(appDatabase: MarvelDatabase): MarvelCharacterDao {
        return appDatabase.marvelCharacterDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): MarvelDatabase {
        return Room.databaseBuilder(
            appContext,
            MarvelDatabase::class.java,
            "MarvelDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageEntityMapper(): Mapper<ImageEntity, Image> = ImageMapper()

    @Provides
    @Singleton
    fun provideUrlEntityMapper(): Mapper<UrlEntity, Url> = UrlMapper()

    @Provides
    @Singleton
    fun provideMarvelCharacterEntityMapper(
        imageMapper: Mapper<ImageEntity, Image>,
        urlMapper: Mapper<UrlEntity, Url>
    ): Mapper<MarvelCharacterEntity, DomainMarvelCharacter> =
        MarvelCharacterMapper(imageMapper, urlMapper)

    @Provides
    @Singleton
    fun provideMarvelCharacterDBEntityMapper():
            TwoWaysMapper<MarvelCharacterDBEntity, DomainMarvelCharacter> =
        MarvelCharacterDBMapper()
}
