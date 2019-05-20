package com.makki.basic.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Maksym.Popovych
 */
@Entity(tableName = "key_value_table")
class KeyValueDbo(
    @PrimaryKey
    @ColumnInfo(name = "entity_key")
    val key: String,
    @ColumnInfo(name = "value")
    val value: String
)

