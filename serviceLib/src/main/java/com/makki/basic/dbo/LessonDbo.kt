package com.makki.basic.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.makki.basic.model.LessonAsset

/**
 * @author Maksym.Popovych
 */
@Entity(tableName = "lesson_table")
class LessonDbo {

    companion object {
        val Null = LessonDbo()
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "subject_id")
    var subjectId: Int = 0

    @ColumnInfo(name = "subject_name")
    var subjectName: String = ""

    @ColumnInfo(name = "period_id")
    var periodId: Int = 0

    @ColumnInfo(name = "month_id")
    var monthId: Int = 0

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "start_time")
    var startTime: Long = 0

    @ColumnInfo(name = "duration")
    var duration: Long = 0

    @ColumnInfo(name = "extra")
    var extra: String = ""

    @Ignore
    var debugSource = "none"

    fun build(): LessonAsset {
        return LessonAsset(id, subjectId, subjectName, periodId, monthId, startTime, duration, extra)
    }
}
