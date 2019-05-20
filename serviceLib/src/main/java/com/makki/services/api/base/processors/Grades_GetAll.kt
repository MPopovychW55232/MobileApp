package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.services.api.base.cms.schedule.GradesApi_General
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class Grades_GetAll : ParcelableListProcessor() {

    val api: GradesApi_General by inject()

    override suspend fun doWork(bundle: Bundle): List<Parcelable> {
        return api.getAllGradePeriods()
    }
}