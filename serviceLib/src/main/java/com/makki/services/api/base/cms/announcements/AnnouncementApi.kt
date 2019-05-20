package com.makki.services.api.base.cms.announcements

import com.makki.basic.dbo.AnnouncementDbo
import com.makki.basic.dbo.NotificationDbo
import org.koin.core.KoinComponent

/**
 * @author Maksym.Popovych
 */

abstract class AnnouncementApi : KoinComponent {
    open suspend fun getById(id: Int): AnnouncementDbo? = null
    open suspend fun getAll(): List<AnnouncementDbo> = emptyList()
    open suspend fun put(list: List<AnnouncementDbo>) {}

    open class General: AnnouncementApi()
    open class Persistent: AnnouncementApi()
    open class External: AnnouncementApi()
}
