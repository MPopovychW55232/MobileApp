package com.makki.services.api.base

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import com.makki.services.api.service.ApiService
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

/**
 * @author Maksym.Popovych
 */
class ServiceCommunication(private val context: Context) {

    private enum class State { Idle, Connecting, Connected, Disconnected, Reconnecting }
    private var state = State.Idle

    private val nullConnection = Connection()
    private var connection = nullConnection

    private val nullMsg = Msg(null)
    private val pendingMsg = Msg(null)
    private val subject = BehaviorSubject.createDefault(nullMsg)

    private var disposable = Disposables.disposed()

    val handler = HandlerThread("LanguageAppServiceThread").also { it.start() }
    val scheduler: Scheduler = AndroidSchedulers.from(handler.looper)

    val messenger: Maybe<Msg> = subject
        .filter { it !== pendingMsg }
        .firstElement()
        .observeOn(scheduler)

    fun start() {
        if (state != State.Idle) {
            return
        }
        connect()
    }

    fun stop() {
        if (state == State.Idle) {
            return
        }

        state = State.Idle
        connection = nullConnection
        disposable.dispose()
        subject.onNext(nullMsg)
    }

    private fun connect() {
        if (state == State.Connected) {
            return
        }

        state = State.Connecting
        connection = nullConnection
        disposable.dispose()
        subject.onNext(pendingMsg)

        val intent = Intent(context, ApiService::class.java)

        connection = Connection()
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun reconnect() {
        if (state == State.Reconnecting) {
            return
        }

        state = State.Reconnecting
        connection = nullConnection
        subject.onNext(pendingMsg)

        disposable.dispose()
        disposable = scheduler.scheduleDirect(::connect, 5, TimeUnit.SECONDS)
    }

    private fun onConnected(new: Connection, binder: IBinder) {
        scheduler.scheduleDirect {
            if (state != State.Connecting) {
                return@scheduleDirect
            }

            state = State.Connected
            connection = new
            disposable.dispose()

            try {
                subject.onNext(Msg(Messenger(binder)))
            } catch (e: Exception) {
                onError()
            }
        }
    }

    private fun onError() {
        try {
            context.unbindService(connection)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        state = State.Disconnected
        connection = nullConnection
        disposable.dispose()
        subject.onNext(pendingMsg)

        reconnect()
    }

    private fun onDisconnected(active: Connection) {
        try {
            context.unbindService(active)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        scheduler.scheduleDirect {
            if (state != State.Connected) {
                return@scheduleDirect
            }

            state = State.Disconnected
            connection = nullConnection
            disposable.dispose()
            subject.onNext(pendingMsg)

            connect()
        }
    }

    private inner class Connection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) = onConnected(this, service)
        override fun onServiceDisconnected(name: ComponentName) = onDisconnected(this)
    }

    data class Msg(val messenger: Messenger?)
}

