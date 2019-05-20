package com.makki.services.api.base.cms.movement

import com.makki.basic.dbo.MovementDbo
import com.makki.services.api.base.apiMethod
import com.makki.services.api.base.database.DatabaseComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class MovementApi_Persistent : MovementApi.Persistent() {

    private val database: DatabaseComponent by inject()

    override suspend fun getAll() =
        apiMethod(fallback = { emptyList<MovementDbo>() }) {
            database.readTransaction {
                it.movementDao.getAll()
            }.map { it.also { it.debugSource = "db" } }.sortedBy { it.getWhenEntered() }
        }

    override suspend fun put(dbo: MovementDbo) =
        apiMethod(fallback = { }) {
            database.writeTransaction {
                it.movementDao.insert(dbo)
                it.movementDao.insertTransition(dbo.entries)
            }
        }
}