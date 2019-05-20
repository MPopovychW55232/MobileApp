package com.makki.services.api.service

import android.app.Service
import android.content.Intent
import android.os.*
import com.makki.services.api.base.RxManager
import com.makki.services.api.base.processors.BaseProcessor
import io.reactivex.disposables.Disposables
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.koin.android.ext.android.get
import org.koin.core.qualifier.named
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Maksym.Popovych
 */
class ApiService : Service() {
    private lateinit var messenger: Messenger
    private val requestMap = ConcurrentHashMap<RequestId, SingleRequest>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY_COMPATIBILITY
    }

    override fun onCreate() {
        super.onCreate()

        val handlerThread = HandlerThread("LanguageService").also { it.start() }
        val handler = Handler(handlerThread.looper, ::handleMessage)
        messenger = Messenger(handler)
    }

    private fun handleMessage(msg: Message): Boolean {
        val id = RequestId(msg.replyTo, msg.arg1)

        when (msg.what) {
            RxManager.NEW_REQUEST_CODE -> {
                val bundle = msg.data ?: Bundle.EMPTY
                bundle.classLoader = javaClass.classLoader

                val messageName = bundle.getString(RxManager.MSG_KEY) ?: "UNKNOWN IN BASE API SERVICE"

                val request = RequestWrapper(id, messageName)
                requestMap[request.id] = request

                handleRequest(request, messageName, bundle)
            }

            RxManager.CANCEL_REQUEST_CODE -> {
                requestMap.remove(id)?.dispose()
            }
        }
        return true
    }

    override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }

    inner class RequestWrapper(id: RequestId, name: String) : SingleRequest(id, name) {
        override fun onSuccess(t: Bundle) {
            super.onSuccess(t)

            requestMap.remove(id)
        }

        override fun onError(e: Throwable) {
            super.onError(e)

            requestMap.remove(id)
        }
    }

    private fun handleRequest(request: SingleRequest, messageName: String, bundle: Bundle) {
        val processor = try {
            get<BaseProcessor>(named(messageName))
        } catch (e: Exception) {
            request.onError(Exception("unknown message: $messageName"))
            return
        }

        bundle.classLoader = javaClass.classLoader
        bundle.keySet()

        val job = GlobalScope.async {
            try {
                request.onSuccess(processor.process(bundle))
            } catch (e: Exception) {
                request.onError(e)
            }
        }

        request.onSubscribe(Disposables.fromAction { job.cancel() })
    }
}