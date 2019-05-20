package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */
@Parcelize
class PeriodAsset(
    val id: String,
    val title: String,
    val startTime: Long,
    val endTime: Long

) : Parcelable {

    fun getDuration() = endTime - startTime

}