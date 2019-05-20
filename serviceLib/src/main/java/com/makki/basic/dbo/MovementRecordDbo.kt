package com.makki.basic.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makki.basic.model.MovementRecord

/**
 * @author Maksym.Popovych
 */

@Entity(tableName = "movement_record_table")
class MovementRecordDbo {

    companion object {
        val Null = MovementRecordDbo()
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "parent_day_id")
    var parentDayId: String = ""

    @ColumnInfo(name = "day_tag")
    var dayTag: String = ""

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0

    @ColumnInfo(name = "gate")
    var gate: String = ""

    @ColumnInfo(name = "entered")
    var entered: Boolean = false

    fun build() = MovementRecord(id, dayTag, timestamp, gate, entered)

}