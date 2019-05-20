package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.services.api.base.cms.announcements.AnnouncementApi
import com.makki.services.api.base.cms.announcements.AnnouncementApi_General
import com.makki.services.api.base.cms.movement.MovementApi_General
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class Announcement_GetAll: ParcelableListProcessor() {

    val api: AnnouncementApi_General by inject()

    override suspend fun doWork(bundle: Bundle): List<Parcelable> {
        return api.getAll().map { it.build() }
    }
}