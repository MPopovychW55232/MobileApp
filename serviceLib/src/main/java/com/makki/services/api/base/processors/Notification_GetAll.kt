package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.services.api.base.cms.notifications.NotificationApi_General
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class Notification_GetAll : ParcelableListProcessor() {

    val api: NotificationApi_General by inject()

    override suspend fun doWork(bundle: Bundle): List<Parcelable> {
        return api.getAll().map { it.build() }
    }
}