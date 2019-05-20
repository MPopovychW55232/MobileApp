package com.makki.services.api.base.cms.notifications

import com.makki.basic.dbo.NotificationDbo
import org.koin.core.KoinComponent

/**
 * @author Maksym.Popovych
 */

abstract class NotificationApi : KoinComponent {
    open suspend fun getById(id: Int): NotificationDbo? = null
    open suspend fun markAsRead(id: Int) {}
    open suspend fun getAll(): List<NotificationDbo> = emptyList()
    open suspend fun put(list: List<NotificationDbo>) {}

    open class General: NotificationApi()
    open class Persistent: NotificationApi()
    open class External: NotificationApi()
}
