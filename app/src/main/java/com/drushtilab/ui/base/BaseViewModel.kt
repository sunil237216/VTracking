package com.drushtilab.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable();

    protected fun addToDisposable(disposable: Disposable) {
        compositeDisposable.remove(disposable)
        compositeDisposable.add(disposable)
    }

    protected fun onStop() {
        compositeDisposable.clear()
    }
}
