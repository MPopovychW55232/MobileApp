package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */
@Parcelize
data class AnnouncementAsset(
    var id: Int = 0,
    var senderId: Int = 0,
    var senderDisplayName: String = "",
    var title: String = "",
    var message: String = "",
    var timestamp: Long = 0
) : Parcelable {

    companion object {
        val Null = AnnouncementAsset(0, 0, "", "", "", 0)
    }

}