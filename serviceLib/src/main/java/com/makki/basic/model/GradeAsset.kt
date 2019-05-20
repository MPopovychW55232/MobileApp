package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */
@Parcelize
data class GradeAsset(
    val id: Int,
    val periodId: Int,
    val subjectId: Int,
    val subjectName: String,
    val value: String,
    val notices: String,
    val timestamp: Long
) : Parcelable