package com.makki.services.api.base.cms.key

import com.makki.basic.dbo.KeyValueDbo
import com.makki.services.api.base.database.DatabaseComponent
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */

class KeyApi : KoinComponent {

    val database: DatabaseComponent by inject()

    suspend fun getValue(key: String): String? =
        database.readTransaction {
            it.keyValueDao.getByKey(key)?.value
        }

    suspend fun put(key: String, value: String) =
        database.writeTransaction {
            it.keyValueDao.insert(KeyValueDbo(key, value))
    }

}