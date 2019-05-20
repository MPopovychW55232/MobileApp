package com.makki.services.api.base.cms.person

import com.makki.basic.dbo.PersonDbo
import com.makki.services.api.base.apiMethod
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class PersonApi_General : PersonApi.General() {
    private val persistentApi: PersonApi_Persistent by inject()
    private val externalApi: PersonApi_External by inject()

    override suspend fun getById(id: Int): PersonDbo? =
        apiMethod {
            persistentApi.getById(id) ?: externalApi.getById(id)
        }

    override suspend fun put(dbo: PersonDbo) {
        persistentApi.put(dbo)
    }
}