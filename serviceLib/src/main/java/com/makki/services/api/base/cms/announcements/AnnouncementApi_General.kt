package com.makki.services.api.base.cms.announcements

import com.makki.basic.dbo.AnnouncementDbo
import com.makki.services.api.base.apiMethod
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */

class AnnouncementApi_General : AnnouncementApi.General() {

    private val externalApi: AnnouncementApi_External by inject()

    override suspend fun getById(id: Int): AnnouncementDbo? =
        apiMethod {
            val ann = externalApi.getById(id)

            put(listOf(ann ?: return null))

            return ann
        }

    override suspend fun getAll(): List<AnnouncementDbo> =
        apiMethod {
            val notifications = externalApi.getAll()
            put(notifications)

            return notifications
        }

    override suspend fun put(list: List<AnnouncementDbo>) =
        apiMethod {
            externalApi.put(list)
        }
}