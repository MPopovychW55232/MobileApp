package com.makki.services.api.base

import android.util.Log
import com.makki.languageapp.lib.utils.TAG
import com.makki.services.api.scheduler.SchedulerDispatcher
import io.reactivex.internal.schedulers.RxThreadFactory
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext

/**
 * @author Maksym.Popovych
 */

object Dispatcher {
    val scheduler = Schedulers.from(Executors.newFixedThreadPool(8, RxThreadFactory("ServiceThreads")))
    val threadDispatcher: CoroutineDispatcher = SchedulerDispatcher("ServiceDispatcher", scheduler)
}

suspend inline fun <T> apiMethod(
    noinline fallback: (() -> T)? = null,
    block: () -> T
): T {
    try {
        return block()
    } catch (e: Exception) {
        if (e !is CancellationException) {
            if (fallback != null) {
                Log.w(TAG, "Handled exception in apiMethod()", e)
            } else {
                Log.e(TAG, "Unhandled exception in apiMethod()", e)
            }
        }
        if (fallback != null) {
            return fallback()
        }
        throw e
    }
}

suspend fun <T> apiAsync(
    fallback: (() -> T)? = null,
    block: suspend () -> T
): Deferred<T> {
    val job = GlobalScope.async(coroutineContext) {
        try {
            block()
        } catch (e: Exception) {
            if (fallback != null) {
                return@async fallback()
            }
            throw e
        }
    }
    job.invokeOnCompletion {
        if (it != null && it !is CancellationException) {
            if (fallback != null) {
                Log.w(TAG, "Handled exception in apiAsync()", it)
            } else {
                Log.e(TAG, "Unhandled exception in apiAsync()", it)
            }
        }
    }
    return job
}
