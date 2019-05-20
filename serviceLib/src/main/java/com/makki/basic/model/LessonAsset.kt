package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */
@Parcelize
data class LessonAsset(
    val id: Int,
    val subjectId: Int,
    val subjectName: String,
    val periodId: Int,
    val monthId: Int,
    val start: Long,
    val duration: Long,
    val extra: String
) : Parcelable {

    companion object {
        val Null = LessonAsset(0, 0, "", 0, 0, 0, 0, "")
    }

}