package me.miguelos.sample.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import me.miguelos.sample.presentation.core.AppSchedulers
import me.miguelos.sample.domain.common.ExecutionSchedulers

@Module
@InstallIn(ApplicationComponent::class)
abstract class ApplicationModule {

    @Binds
    internal abstract fun bindContext(application: Application): Context

    @Binds
    internal abstract fun bindExecutionSchedulers(
        appSchedulers: AppSchedulers
    ): ExecutionSchedulers
}
