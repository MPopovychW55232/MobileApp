package com.makki.services.api.base.cms.notifications

import com.makki.basic.dbo.NotificationDbo
import com.makki.services.api.base.apiMethod
import com.makki.services.api.base.database.DatabaseComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */

class NotificationApi_Persistent : NotificationApi.Persistent() {

    private val database: DatabaseComponent by inject()

    override suspend fun getById(id: Int) =
        apiMethod(fallback = { null as NotificationDbo? }) {
            database.readTransaction {
                it.notificationDao.getById(id)
            }
        }

    override suspend fun getAll(): List<NotificationDbo> =
        apiMethod(fallback = { emptyList() }) {
            database.readTransaction {
                it.notificationDao.getAll()
            }
        }

    override suspend fun put(list: List<NotificationDbo>) =
        apiMethod(fallback = { }) {
            database.writeTransaction {
                it.notificationDao.insert(list)
            }
        }
}