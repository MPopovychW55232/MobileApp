package com.makki.services.api.base

import android.os.Bundle
import android.os.Parcelable
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
abstract class ManagerBase : KoinComponent {

    companion object {
        const val DATA_KEY = "DATA"
    }

    private val manager: RxManager by inject()

    fun send(message: String, bundle: Bundle?): Single<Bundle> {
        return manager.send(message, bundle)
            .doOnEvent { bundle1, _ -> bundle1?.keySet() }
    }

    fun send(message: Enum<*>, bundle: Bundle?) = send(message.name, bundle)

    fun sendAsCompletable(message: Enum<*>, input: Bundle?): Completable {
        return send(message, input).ignoreElement()
    }

    inline fun sendAsCompletable(message: Enum<*>, block: Bundle.() -> Unit = {}): Completable =
        sendAsCompletable(message, Bundle().also { it.block() })

    fun <T : Parcelable> sendAsSingle(message: Enum<*>, input: Bundle?, output: String): Single<T> =
        send(message, input).map { bundle -> bundle.getParcelable<T>(output) }

    fun <T : Parcelable> sendAsSingle(message: Enum<*>, input: Bundle?): Single<T> =
        sendAsSingle(message, input, DATA_KEY)

    inline fun <T : Parcelable> sendAsSingle(message: Enum<*>, block: Bundle.() -> Unit = {}): Single<T> =
        sendAsSingle(message, Bundle().also { it.block() }, DATA_KEY)

    fun <T : Parcelable> sendAsMaybe(message: Enum<*>, input: Bundle?, output: String): Maybe<T> =
        send(message, input).flatMapMaybe { bundle ->
            val item = bundle.getParcelable<T>(output)
            if (item == null) {
                Maybe.empty()
            } else Maybe.just(item)
        }

    fun <T : Parcelable> sendAsMaybe(message: Enum<*>, input: Bundle?): Maybe<T> =
        sendAsMaybe(message, input, DATA_KEY)

    inline fun <T : Parcelable> sendAsMaybe(message: Enum<*>, block: Bundle.() -> Unit = {}): Maybe<T> =
        sendAsMaybe(message, Bundle().also { it.block() }, DATA_KEY)

    fun <T : Parcelable> sendAsObservable(message: Enum<*>, input: Bundle?, output: String): Observable<T> =
        send(message, input).flattenAsObservable { bundle ->
            bundle.getParcelableArrayList(output) ?: emptyList<T>()
        }

    fun <T : Parcelable> sendAsObservable(message: Enum<*>, input: Bundle?): Observable<T> =
        sendAsObservable(message, input, DATA_KEY)

    inline fun <T : Parcelable> sendAsObservable(
        message: Enum<*>,
        block: Bundle.() -> Unit = {}
    ): Observable<T> =
        sendAsObservable(message, Bundle().also { it.block() }, DATA_KEY)

}