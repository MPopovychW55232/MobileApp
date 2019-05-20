package com.makki.services.api.base.cms.movement

import com.makki.basic.dbo.MovementDbo
import com.makki.languageapp.lib.utils.takeIfEmpty
import com.makki.services.api.base.apiMethod
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class MovementApi_General : MovementApi.General() {

    private val persistentApi: MovementApi_Persistent by inject()
    private val externalApi: MovementApi_External by inject()

    override suspend fun getAll() =
        apiMethod(fallback = { emptyList<MovementDbo>() }) {
//            val list = persistentApi.getAll().takeIfEmpty { externalApi.getAll() }
            val list = externalApi.getAll()
            putAll(list)

            return list
        }

    override suspend fun getByRange(start: Long, end: Long): List<MovementDbo> {
        return externalApi.getByRange(start, end).filter { start < it.getWhenEntered() && it.getWhenLeft() < end }
    }

    override suspend fun put(dbo: MovementDbo) =
        apiMethod(fallback = { }) {
            persistentApi.put(dbo)
        }

    suspend fun putAll(list: List<MovementDbo>) =
        apiMethod(fallback = { }) {
            for (dbo in list) {
                put(dbo)
            }
        }
}