package com.makki.basic.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makki.basic.model.AnnouncementAsset
import com.makki.basic.model.NotificationAsset

/**
 * @author Maksym.Popovych
 */

@Entity(tableName = "announcement_table")
class AnnouncementDbo {

    companion object {
        val Null = AnnouncementDbo()
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "sender_id")
    var senderId: Int = 0

    @ColumnInfo(name = "sender_name")
    var senderName: String = ""

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "message")
    var message: String = ""

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0

    fun build() = AnnouncementAsset(id, senderId, senderName, title, message, timestamp)
}