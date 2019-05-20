package com.makki.services.api.base.database.dao

import androidx.room.*
import com.makki.basic.dbo.MovementDbo
import com.makki.basic.dbo.MovementRecordDbo
import com.makki.services.api.base.database.DaoBase

/**
 * @author Maksym.Popovych
 */
@Dao
abstract class MovementDao : DaoBase<MovementDbo>(MovementDbo::class.java) {

    @Query("select * from movement_table where id = :id limit 1")
    abstract fun getDaysById(id: String): MovementDbo?

    @Query("select * from movement_table")
    abstract fun getDaysAll(): List<MovementDbo>

    @Query("select * from movement_record_table where parent_day_id in (:ids)")
    abstract fun getTransitionsById(ids: String): List<MovementRecordDbo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTransition(collection: Collection<MovementRecordDbo>)

    @Transaction
    open fun getById(id: String): MovementDbo {
        return getDaysById(id)?.also {
            it.entries = getTransitionsById(it.id)
        } ?: MovementDbo.Null
    }

    @Transaction
    open fun getAll(): List<MovementDbo> {
        return getDaysAll().map {
           it.also {  it.entries = getTransitionsById(it.id) }
        }
    }

}