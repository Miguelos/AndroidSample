package me.miguelos.sample.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.miguelos.sample.domain.common.ExecutionSchedulers
import me.miguelos.sample.presentation.core.AppSchedulers

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    internal abstract fun bindContext(application: Application): Context

    @Binds
    internal abstract fun bindExecutionSchedulers(
        appSchedulers: AppSchedulers
    ): ExecutionSchedulers
}
