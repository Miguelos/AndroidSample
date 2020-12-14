package me.miguelos.sample.presentation.core

import androidx.annotation.VisibleForTesting
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import me.miguelos.sample.domain.common.ExecutionSchedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSchedulers @VisibleForTesting constructor(
    val io: Scheduler,
    val computation: Scheduler,
    val ui: Scheduler
) : ExecutionSchedulers {

    @Inject
    constructor() : this(
        Schedulers.io(),
        Schedulers.computation(),
        AndroidSchedulers.mainThread()
    )

    override fun io(): Scheduler = io
    override fun computation(): Scheduler = computation
    override fun ui(): Scheduler = ui
}
