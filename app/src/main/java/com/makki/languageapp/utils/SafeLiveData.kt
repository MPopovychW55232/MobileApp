package com.makki.languageapp.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * @author Maksym.Popovych
 */
open class SafeLiveData<T>(default: T) : MutableLiveData<T>() {

    init {
        value = default
    }

    override fun getValue(): T {
        return super.getValue()!!
    }

    @MainThread
    override fun setValue(value: T) {
        super.setValue(value)
    }

    @Suppress("RedundantOverride")
    override fun postValue(value: T) {
        super.postValue(value)
    }

    @MainThread
    inline fun observe(owner: LifecycleOwner, crossinline block: (T) -> Unit) {
        super.observe(owner, Observer { block(it!!) })
    }

    @MainThread
    inline fun observeForever(crossinline block: (T) -> Unit) {
        super.observeForever { block(it!!) }
    }

}