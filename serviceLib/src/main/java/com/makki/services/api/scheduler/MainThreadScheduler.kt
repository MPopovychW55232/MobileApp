package com.makki.services.api.scheduler

import android.os.Handler
import android.os.Looper
import android.os.Message
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import java.util.concurrent.TimeUnit

/**
 * @author Maksym.Popovych
 */

object MainSchedulers {

    private val sMainThreadImmediate = MainThreadScheduler(true)
    private val sMainThreadAlwaysPost = MainThreadScheduler(false)

    fun mainThreadImmediate(): Scheduler {
        return sMainThreadImmediate
    }

    fun mainThreadAlwaysPost(): Scheduler {
        return sMainThreadAlwaysPost
    }

}

class MainThreadScheduler(private val immediate: Boolean) : Scheduler() {

    companion object {
        private val handler = Handler(Looper.getMainLooper())
    }

    override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
        if (immediate && delay <= 0 && Thread.currentThread() === handler.looper.thread) {
            run.run()
            return Disposables.disposed()
        }

        val action = MainThreadAction(
            handler,
            run
        )
        handler.postDelayed(action, unit.toMillis(delay))
        return action
    }

    override fun createWorker(): Scheduler.Worker {
        return MainThreadWorker(
            handler,
            immediate
        )
    }
}

private class MainThreadAction(private val handler: Handler, private val runnable: Runnable) : Runnable, Disposable {

    private var disposed: Boolean = false

    override fun run() {
        runnable.run()
    }

    override fun dispose() {
        disposed = true
        handler.removeCallbacks(this)
    }

    override fun isDisposed(): Boolean {
        return disposed
    }
}

private class MainThreadWorker(private val handler: Handler, private val immediate: Boolean) : Scheduler.Worker() {

    private var disposed: Boolean = false

    override fun schedule(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
        if (immediate && delay <= 0 && Thread.currentThread() === handler.looper.thread) {
            run.run()
            return Disposables.disposed()
        }

        val action = MainThreadAction(handler, run)

        val message = Message.obtain(handler, action)
        message.obj = this

        handler.sendMessageDelayed(message, unit.toMillis(delay))
        return action
    }

    override fun dispose() {
        disposed = true
        handler.removeCallbacksAndMessages(this)
    }

    override fun isDisposed(): Boolean {
        return disposed
    }

}