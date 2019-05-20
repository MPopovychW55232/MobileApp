package com.makki.basic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Maksym.Popovych
 */
@Parcelize
data class NotificationAsset(
    var id: Int = 0,
    var receiverId: Int = 0,
    var senderId: Int = 0,
    var senderDisplayName: String = "",
    var title: String = "",
    var message: String = "",
    var fresh: Boolean = false,
    var timestamp: Long = 0
) : Parcelable {

    companion object {
        val Null = NotificationAsset(0, 0, 0, "", "", "", false, 0)
    }

}