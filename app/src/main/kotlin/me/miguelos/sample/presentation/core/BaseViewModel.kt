package me.miguelos.sample.presentation.core

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber


abstract class BaseViewModel : ViewModel() {

    private val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    protected fun addDisposable(disposable: Disposable) {
        Timber.tag("BaseViewModel")
            .d("Add disposable $disposable")
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
