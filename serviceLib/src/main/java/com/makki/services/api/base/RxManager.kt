package com.makki.services.api.base

import android.app.Application
import android.os.*
import android.util.Log
import android.util.SparseArray
import com.makki.basic.model.ServiceException
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.disposables.Disposables
import io.reactivex.functions.Action
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
open class RxManager(application: Application) : KoinComponent {

    companion object {
        const val MSG_KEY = "MSGKEY"
        const val NEW_REQUEST_CODE = 0
        const val CANCEL_REQUEST_CODE = 1
    }

    val manager: ServiceCommunication by inject()

    private val localMessenger = Messenger(Handler(manager.handler.looper, ::handleLocalMsg))
    private val lifeCycleTracker = LifeCycleTracker(application, manager.scheduler, ::onLifecycleChanged)

    private var reqCounter = 0
    private val pendingRequests = SparseArray<Request>()

    init {
        RxJavaPlugins.setErrorHandler { throwable ->
            Log.e("LangApp", "unhandled RxJava exception", throwable)
        }
    }

    fun send(messageKey: String, bundle: Bundle?): Single<Bundle> {
        var b = bundle
        if (b == null || b == Bundle.EMPTY) {
            b = Bundle()
        }

        b.putString(MSG_KEY, messageKey)

        return manager.messenger
            .observeOn(manager.scheduler)
            .flatMapSingle { Single.create(MessageSingle(it.messenger, b, Exception())) }
            .doFinally { lifeCycleTracker.finishRequest() }
            .doOnSubscribe { lifeCycleTracker.startRequest() }
            .subscribeOn(manager.scheduler)
            .unsubscribeOn(manager.scheduler)
    }

    private fun onLifecycleChanged(connected: Boolean) {
        if (connected) {
            manager.start()
        } else {
            manager.stop()
        }
    }

    private fun handleLocalMsg(msg: Message): Boolean {
        val requestId = msg.arg1
        val info = pendingRequests.get(requestId) ?: return true

        pendingRequests.remove(requestId)

        val bundle = msg.data

        Schedulers.io().scheduleDirect {
            bundle.classLoader = javaClass.classLoader

            val errorMsg = bundle.getString("error")
            val exception = bundle.getParcelable<ServiceException>("error")
            when {
                errorMsg != null -> info.emitter.onError(Exception(errorMsg))
                exception != null -> info.emitter.onError(exception)
                else -> info.emitter.onSuccess(bundle)
            }
        }
        return true
    }

    inner class MessageSingle(val messenger: Messenger?, val bundle: Bundle, val exception: Exception) :
        SingleOnSubscribe<Bundle> {
        override fun subscribe(emitter: SingleEmitter<Bundle>) {
            val messenger = messenger
            if (messenger == null) {
                emitter.onError(Exception("messenger is null", exception))
                return
            }

            val message = Message.obtain()
            message.what = NEW_REQUEST_CODE
            message.data = bundle
            message.arg1 = ++reqCounter
            message.replyTo = localMessenger

            val info = Request(message.arg1, emitter, messenger, exception)
            try {
                info.linkToDeath()
                messenger.send(message)
                pendingRequests.put(message.arg1, info)
            } catch (ex: Throwable) {
                emitter.onError(info.wrapException(ex))
            }

            emitter.setDisposable(Disposables.fromAction(DisposeAction(message.arg1)))
        }
    }

    private inner class Request(
        val requestId: Int,
        val emitter: SingleEmitter<Bundle>,
        val messenger: Messenger,
        val exception: Exception
    ) : IBinder.DeathRecipient {

        fun wrapException(throwable: Throwable) = throwable

        fun linkToDeath() {
            messenger.binder.linkToDeath(this, 0)
        }

        fun unlinkToDeath() {
            messenger.binder.unlinkToDeath(this, 0)
        }

        override fun binderDied() {
            manager.scheduler.scheduleDirect {
                emitter.onError(wrapException(Exception("binder died")))
                pendingRequests.remove(requestId)
            }
        }
    }

    private inner class DisposeAction(val requestId: Int) : Action {
        override fun run() {
            val info = pendingRequests.get(requestId) ?: return
            pendingRequests.remove(requestId)

            val message = Message.obtain()
            message.what = CANCEL_REQUEST_CODE
            message.arg1 = requestId
            message.replyTo = localMessenger
            try {
                info.messenger.send(message)
                info.unlinkToDeath()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}