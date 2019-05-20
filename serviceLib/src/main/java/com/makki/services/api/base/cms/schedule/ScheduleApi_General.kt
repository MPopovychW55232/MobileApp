package com.makki.services.api.base.cms.schedule

import com.makki.basic.dbo.LessonDbo
import com.makki.basic.dbo.PeriodDbo
import com.makki.basic.dbo.SubjectDbo
import com.makki.languageapp.lib.utils.merge
import com.makki.services.api.base.apiAsync
import com.makki.services.api.base.apiMethod
import kotlinx.coroutines.awaitAll
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */

class ScheduleApi_General : ScheduleApi.General() {

    private val externalApi: ScheduleApi_External by inject()

    override suspend fun getAllPeriods(): List<PeriodDbo> =
        apiMethod {
            externalApi.getAllPeriods()
        }

    override suspend fun getActivePeriods(): List<PeriodDbo> =
        apiMethod {
            externalApi.getActivePeriods()
        }

    override suspend fun getPeriodById(id: Int): PeriodDbo? =
        apiMethod {
            externalApi.getPeriodById(id)
        }

    override suspend fun getSubjectById(id: Int): SubjectDbo? =
        apiMethod {
            externalApi.getSubjectById(id)
        }

    override suspend fun getSubjectsByPeriodId(id: Int): List<SubjectDbo> =
        apiMethod {
            externalApi.getSubjectsByPeriodId(id)
        }

    override suspend fun getLessonsByPeriodId(id: Int): List<LessonDbo> =
        apiMethod {
            externalApi.getLessonsByPeriodId(id)
        }

    override suspend fun getLessonsBySubjectId(id: Int): List<LessonDbo> =
        apiMethod {
            externalApi.getLessonsBySubjectId(id)
        }

    override suspend fun getActiveLessons(): List<LessonDbo> =
        apiMethod {
            return getActivePeriods().map {
                apiAsync { getLessonsByPeriodId(it.id) }
            }.awaitAll().merge().sortedBy { it.startTime }
        }
}
