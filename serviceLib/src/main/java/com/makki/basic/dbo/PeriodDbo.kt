package com.makki.basic.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * @author Maksym.Popovych
 */
@Entity(tableName = "period_table")
class PeriodDbo {

    companion object {
        val Null = PeriodDbo()
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "start_time")
    var startTime: Long = 0

    @ColumnInfo(name = "end_time")
    var endTime: Long = 0

    @ColumnInfo(name = "description")
    var description: String = ""

    @ColumnInfo(name = "modify_timestamp")
    var modified: Long = 0

    @Ignore
    var debugSource = "none"

}
