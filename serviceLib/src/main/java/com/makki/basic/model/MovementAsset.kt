package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize


/**
 * @author Maksym.Popovych
 */
@Parcelize
data class MovementAsset(
    val id: String,
    var entries: List<MovementRecord>
) : Parcelable {

    companion object {
        val Null = MovementAsset("", emptyList())
    }

    @IgnoredOnParcel
    val entered = entries.filter { it.entered }.minBy { it.timestamp }?.timestamp ?: 0

    @IgnoredOnParcel
    val left = entries.filter { !it.entered }.maxBy { it.timestamp }?.timestamp ?: 0

    fun getInDuration(): Long = left - entered
}