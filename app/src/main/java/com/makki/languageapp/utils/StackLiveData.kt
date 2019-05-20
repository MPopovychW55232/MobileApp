package com.makki.languageapp.utils

/**
 * @author Maksym.Popovych
 */
class StackLiveData<T>(initial: T, private val empty: T) : SafeLiveData<T>(initial) {

    private val entries = LinkedHashSet(listOf(initial))

    fun popValue(block: (T) -> Unit) {
        var nextEntry: T
        synchronized(entries) {
            entries.remove(entries.lastOrNull())
            nextEntry = entries.lastOrNull() ?: empty
        }

        block(nextEntry ?: empty)
        super.postValue(nextEntry ?: empty)
    }

    override fun postValue(value: T) {
        synchronized(entries) {
            entries.remove(value)
            entries.add(value)
        }

        super.postValue(value)
    }
}