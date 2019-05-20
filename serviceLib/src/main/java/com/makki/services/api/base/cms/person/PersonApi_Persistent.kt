package com.makki.services.api.base.cms.person

import com.makki.basic.dbo.PersonDbo
import com.makki.services.api.base.apiMethod
import com.makki.services.api.base.database.DatabaseComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class PersonApi_Persistent : PersonApi.Persistent() {

    private val database: DatabaseComponent by inject()

    override suspend fun getById(id: Int): PersonDbo? =
        apiMethod {
            database.readTransaction {
                it.personDao.getById(id)
            }
        }

    override suspend fun put(dbo: PersonDbo) =
        apiMethod {
            database.writeTransaction {
                it.personDao.insert(dbo)
            }
        }

}