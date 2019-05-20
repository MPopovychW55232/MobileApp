package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.services.api.base.cms.schedule.ScheduleApi_General
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class Schedule_GetAllActive : ParcelableListProcessor() {
    val api: ScheduleApi_General by inject()

    override suspend fun doWork(bundle: Bundle): List<Parcelable> {
        return api.getActiveLessons().map { it.build() }
    }
}