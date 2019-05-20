package com.makki.services.api.base.cms.notifications

import com.makki.basic.dbo.NotificationDbo
import com.makki.languageapp.lib.utils.takeIfEmpty
import com.makki.services.api.base.apiAsync
import com.makki.services.api.base.apiMethod
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */

class NotificationApi_General : NotificationApi.General() {

    private val persistentApi: NotificationApi_Persistent by inject()
    private val externalApi: NotificationApi_External by inject()

    override suspend fun getById(id: Int): NotificationDbo? =
        apiMethod {
            val notification = persistentApi.getById(id)
                ?: externalApi.getById(id)

            notification?.fresh = false

            put(listOf(notification ?: return null))

            return notification
        }

    override suspend fun markAsRead(id: Int) =
        apiMethod {
            arrayOf(apiAsync { persistentApi.markAsRead(id) },
                apiAsync { externalApi.markAsRead(id) }
            ).map { it.await() }
            return@apiMethod
        }

    override suspend fun getAll(): List<NotificationDbo> =
        apiMethod {
            val notifications = persistentApi.getAll().takeIfEmpty { externalApi.getAll() }
            put(notifications)

            return notifications
        }

    override suspend fun put(list: List<NotificationDbo>) =
        apiMethod {
            arrayOf(apiAsync { persistentApi.put(list) },
                apiAsync { externalApi.put(list) }
            ).map { it.await() }
            return@apiMethod
        }
}