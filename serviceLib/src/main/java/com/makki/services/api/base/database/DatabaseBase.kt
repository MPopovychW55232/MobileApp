package com.makki.services.api.base.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase
import com.makki.basic.dbo.*
import com.makki.services.api.base.database.dao.MovementDao
import com.makki.services.api.base.database.dao.NotificationDao
import com.makki.services.api.base.database.dao.PeriodDao
import com.makki.services.api.base.database.dao.PersonDao

/**
 * @author Maksym.Popovych
 */

@Database(
    version = 1, entities = [
        KeyValueDbo::class,
        PeriodDbo::class,
        NotificationDbo::class,
        MovementDbo::class,
        MovementRecordDbo::class,
        PersonDbo::class
    ]
)

abstract class DatabaseBase : RoomDatabase() {

    abstract val keyValueDao: KeyValueDao
    abstract val periodDao: PeriodDao
    abstract val notificationDao: NotificationDao
    abstract val movementDao: MovementDao
    abstract val personDao: PersonDao

}

//ref
@Dao
abstract class KeyValueDao : DaoBase<KeyValueDbo>(KeyValueDbo::class.java) {
    @Query("select * from key_value_table where entity_key = :key limit 1")
    abstract fun getByKey(key: String): KeyValueDbo?
}

