package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */

@Parcelize
data class ServiceException(val msg: String, val code: Int = -1, var data: Parcelable? = null) : Exception(), Parcelable {
    override val message: String?
        get() = msg
}