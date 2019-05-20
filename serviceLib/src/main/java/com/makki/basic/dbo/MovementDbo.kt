package com.makki.basic.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.makki.basic.model.MovementAsset

/**
 * @author Maksym.Popovych
 */
@Entity(tableName = "movement_table")
class MovementDbo {

    companion object {
        val Null = MovementDbo()
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""

    @Ignore
    var entries = emptyList<MovementRecordDbo>()

    @Ignore
    var debugSource = "none"

    fun getWhenEntered(): Long {
        return entries.filter { it.entered }.minBy { it.timestamp }?.timestamp ?: 0
    }

    fun getWhenLeft(): Long {
        return entries.filter { !it.entered }.maxBy { it.timestamp }?.timestamp ?: 0
    }

    fun getInDuration(): Long = getWhenLeft() - getWhenEntered()

    fun isValid() = entries.isNotEmpty()

    fun build(): MovementAsset {
        if (this == Null || !isValid()) return MovementAsset.Null
        return MovementAsset(id, entries.map { it.build() })
    }

}
