package com.makki.basic.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makki.basic.model.NotificationAsset

/**
 * @author Maksym.Popovych
 */

@Entity(tableName = "notification_table")
class NotificationDbo {

    companion object {
        val Null = NotificationDbo()
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "receiver_id")
    var receiverId: Int = 0

    @ColumnInfo(name = "sender_id")
    var senderId: Int = 0

    @ColumnInfo(name = "sender_name")
    var senderName: String = ""

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "message")
    var message: String = ""

    @ColumnInfo(name = "fresh")
    var fresh: Boolean = false

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0

    fun build() = NotificationAsset(id, receiverId, senderId, senderName, title, message, fresh, timestamp)
}