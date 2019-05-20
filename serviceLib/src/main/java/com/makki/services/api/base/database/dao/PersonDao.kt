package com.makki.services.api.base.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.makki.basic.dbo.PersonDbo
import com.makki.services.api.base.database.DaoBase

/**
 * @author Maksym.Popovych
 */
@Dao
abstract class PersonDao : DaoBase<PersonDbo>(PersonDbo::class.java) {

    @Query("select * from person_table where id = :id limit 1")
    abstract fun getById(id: Int): PersonDbo?

    @Query("select * from person_table")
    abstract fun getAll(): List<PersonDbo>

}