package com.makki.services.api.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.makki.services.api.scheduler.MainSchedulers
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposables
import io.reactivex.disposables.SerialDisposable
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * @author Maksym.Popovych
 */
internal class LifeCycleTracker(
    private val context: Application,
    private val scheduler: Scheduler,
    private val callback: (Boolean) -> Unit
) {

    val isConnected: Boolean
        get() = activityCounter > 0 && requestCounter > 0

    private var requestCounter = 0
    private var activityCounter = 0
    private var disposable = SerialDisposable()
    private val timeout = TimeUnit.MINUTES.toMillis(1)

    init {
        MainSchedulers.mainThreadImmediate().scheduleDirect {
            for (index in 0 until AppTracker.createdActivities.size) {
                onActivityCreated()
            }
            context.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    onActivityCreated()
                }

                override fun onActivityDestroyed(activity: Activity) {
                    onActivityDestroyed()
                }

                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
            })
        }
    }

    fun startRequest() {
        ++requestCounter

        if (activityCounter == 0 && requestCounter == 1) {
            connect()
        }
    }

    fun finishRequest() {
        --requestCounter

        if (activityCounter == 0 && requestCounter == 0) {
            disconnect()
        }
    }

    private fun onActivityCreated() = scheduler.scheduleDirect {
        ++activityCounter

        if (activityCounter == 1 && requestCounter == 0) {
            connect()
        }
    }

    private fun onActivityDestroyed() = scheduler.scheduleDirect {
        --activityCounter

        if (activityCounter == 0 && requestCounter == 0) {
            disconnect()
        }
    }

    private fun connect() {
        disposable.set(Disposables.disposed())
        callback(true)
    }

    private fun disconnect() {
        disposable.set(scheduler.scheduleDirect(::disconnectActual, timeout, TimeUnit.MILLISECONDS))
    }

    private fun disconnectActual() {
        if (activityCounter == 0 && requestCounter == 0) {
            callback(false)
        }
    }
}

object AppTracker {
    var context: Application
        get() = applicationRef.get()
        set(value) = initialize(value)

    val pausedActivities: List<Activity> get() = lifecycle.pausedActivities
    val startedActivities: List<Activity> get() = lifecycle.startedActivities
    val createdActivities: List<Activity> get() = lifecycle.createdActivities

    private val lifecycle = Lifecycle()
    private val applicationRef = AtomicReference<Application>()

    private fun initialize(context: Context) {
        val application = context.applicationContext as Application
        if (applicationRef.compareAndSet(null, application)) {
            application.registerActivityLifecycleCallbacks(lifecycle)
        }
    }

    private class Lifecycle : Application.ActivityLifecycleCallbacks {
        val pausedActivities = CopyOnWriteArrayList<Activity>()
        val startedActivities = CopyOnWriteArrayList<Activity>()
        val createdActivities = CopyOnWriteArrayList<Activity>()

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            createdActivities.add(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            startedActivities.add(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            pausedActivities.add(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            createdActivities.remove(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            createdActivities.remove(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            createdActivities.remove(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        }
    }

}