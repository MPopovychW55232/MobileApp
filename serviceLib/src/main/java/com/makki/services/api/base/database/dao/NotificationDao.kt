package com.makki.services.api.base.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.makki.basic.dbo.NotificationDbo
import com.makki.services.api.base.database.DaoBase

/**
 * @author Maksym.Popovych
 */
@Dao
abstract class NotificationDao : DaoBase<NotificationDbo>(NotificationDbo::class.java) {

    @Query("select * from notification_table where id = :id limit 1")
    abstract fun getById(id: Int): NotificationDbo?

    @Query("select * from notification_table")
    abstract fun getAll(): List<NotificationDbo>

}