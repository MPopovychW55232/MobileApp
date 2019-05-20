package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.services.api.base.cms.movement.MovementApi_General
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class Movement_GetByRange : ParcelableListProcessor() {

    val api: MovementApi_General by inject()

    override suspend fun doWork(bundle: Bundle): List<Parcelable> {
        val start = bundle.getLong("start", -1)
        val end = bundle.getLong("end", -1)

        if (end < start || start == -1L || end == -1L) return emptyList()

        return api.getByRange(start, end).map { it.build() }
    }
}