package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */

@Parcelize
data class MovementRecord(
    var id: Int,
    var dayTag: String,
    var timestamp: Long,
    var gate: String,
    var entered: Boolean
) : Parcelable {

    companion object {
        val Null = MovementRecord(0, "", 0, "", false)
    }

}