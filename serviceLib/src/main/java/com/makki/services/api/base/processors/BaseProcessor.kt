package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.services.api.base.ManagerBase.Companion.DATA_KEY
import com.makki.languageapp.lib.utils.asArrayList
import org.koin.core.KoinComponent

/**
 * @author Maksym.Popovych
 */
abstract class BaseProcessor : KoinComponent {
    abstract suspend fun process(bundle: Bundle): Bundle
}

abstract class SimpleProcessor : BaseProcessor() {
    abstract suspend fun doWork(bundle: Bundle)

    final override suspend fun process(bundle: Bundle): Bundle {
        doWork(bundle)
        return Bundle.EMPTY
    }
}

abstract class StringProcessor : BaseProcessor() {
    abstract suspend fun doWork(bundle: Bundle): String?

    final override suspend fun process(bundle: Bundle) = Bundle().apply {
        putString(DATA_KEY, doWork(bundle))
    }
}

abstract class ParcelableProcessor : BaseProcessor() {
    abstract suspend fun doWork(bundle: Bundle): Parcelable?

    final override suspend fun process(bundle: Bundle) = Bundle().apply {
        putParcelable(DATA_KEY, doWork(bundle))
    }
}

abstract class ParcelableListProcessor : BaseProcessor() {
    abstract suspend fun doWork(bundle: Bundle): List<Parcelable>

    final override suspend fun process(bundle: Bundle) = Bundle().apply {
        putParcelableList(DATA_KEY, doWork(bundle))
    }
}

fun <T : Parcelable> Bundle.putParcelableList(key: String, list: List<T>) = apply {
    putParcelableArrayList(key, list.asArrayList())
}