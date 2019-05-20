package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.services.api.base.cms.movement.MovementApi_General
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class Movement_GetAll: ParcelableListProcessor() {

    val api: MovementApi_General by inject()

    override suspend fun doWork(bundle: Bundle): List<Parcelable> {
        return api.getAll().map { it.build() }
    }
}