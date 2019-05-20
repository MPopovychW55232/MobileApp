package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */
@Parcelize
data class LoginResponse(val msg: String, val success: Boolean) : Parcelable