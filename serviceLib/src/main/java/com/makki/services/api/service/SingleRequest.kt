package com.makki.services.api.service

import android.os.*
import com.makki.basic.model.ServiceException
import io.reactivex.observers.DisposableSingleObserver
import java.util.concurrent.atomic.AtomicReference

/**
 * @author Maksym.Popovych
 */
open class SingleRequest(val id: RequestId, val name: String) : DisposableSingleObserver<Bundle>(), IBinder.DeathRecipient {

    private val messenger = AtomicReference<Messenger>(id.msg)

    init {
        linkToDeath()
    }

    override fun onSuccess(t: Bundle) {
        val messenger = messenger.getAndSet(null)
        if (messenger != null) {
            reply(t, messenger, 0)
        }
    }

    override fun onError(e: Throwable) {
        val messenger = messenger.getAndSet(null)
        if (messenger != null) {
            reply(e, messenger, 0)
        }
    }

    override fun binderDied() {
        val messenger = messenger.getAndSet(null)
        if (messenger != null) {
            reply(Bundle.EMPTY, messenger, 0)
        }
    }

    private fun linkToDeath() {
        val messenger = messenger.get() ?: return
        try {
            messenger.binder.linkToDeath({ onError(Exception("binder died")) }, 0)
        } catch (ex: Exception) {
            onError(Exception("failed to link binder"))
        }

    }

    private fun reply(bundle: Bundle, messenger: Messenger, retryCount: Int) {
        if (retryCount > 2) {
            return
        }
        val parcel = Parcel.obtain()
        try {
            parcel.writeParcelable(bundle, bundle.describeContents())

            val message = Message.obtain()
            message.data = bundle
            message.arg1 = id.id

            messenger.send(message)
        } catch (ex: Exception) {
            reply(ex, messenger, retryCount + 1)
        } finally {
            parcel.recycle()
        }
    }

    private fun reply(e: Throwable, messenger: Messenger, retryCount: Int) {
        val bundle = Bundle()
        if (e is ServiceException) {
            bundle.putParcelable("error", e)
        } else {
            bundle.putString("error", "unhandled exception, ${e.message}")
        }
        reply(bundle, messenger, retryCount)
    }

}