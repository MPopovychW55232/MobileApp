package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.services.api.base.cms.movement.MovementApi_General
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class Movement_GetByDayId : ParcelableProcessor() {

    val api: MovementApi_General by inject()

    override suspend fun doWork(bundle: Bundle): Parcelable? {
        return api.getAll().find { it.id == bundle.getString("id") }?.build()
    }
}