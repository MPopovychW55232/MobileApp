package com.makki.services.api.base.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

/**
 * @author Maksym.Popovych
 */

abstract class DaoBase<T>(val cls: Class<T>) {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg args: T)

    @Delete
    abstract fun delete(vararg args: T)

    @Suppress("UNCHECKED_CAST")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(collection: Collection<T>) {
        val list = collection.toList()
        val array = java.lang.reflect.Array.newInstance(cls, list.size) as Array<T>
        for (index in list.indices) {
            array[index] = list[index]
        }
        insert(*array)
    }

}
