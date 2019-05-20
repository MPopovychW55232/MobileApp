package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */
@Parcelize
data class GradePeriodAsset(
    val periodId: Int,
    val periodName: String,
    val start: Long,
    val end: Long,
    val modified: Long,
    val grades: List<GradeAsset>
) : Parcelable