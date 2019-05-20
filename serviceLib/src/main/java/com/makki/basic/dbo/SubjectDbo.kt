package com.makki.basic.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Maksym.Popovych
 */
@Entity(tableName = "subject_table")
class SubjectDbo {

    companion object {
        val Null = SubjectDbo()
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "period_id")
    var periodId: Int = 0

    @ColumnInfo(name = "description")
    var description: String = ""

}
