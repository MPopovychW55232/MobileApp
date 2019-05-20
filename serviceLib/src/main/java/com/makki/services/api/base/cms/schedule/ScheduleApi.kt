package com.makki.services.api.base.cms.schedule

import com.makki.basic.dbo.LessonDbo
import com.makki.basic.dbo.PeriodDbo
import com.makki.basic.dbo.SubjectDbo
import org.koin.core.KoinComponent

/**
 * @author Maksym.Popovych
 */

abstract class ScheduleApi : KoinComponent {

    open suspend fun getAllPeriods(): List<PeriodDbo> = emptyList()
    open suspend fun getActivePeriods(): List<PeriodDbo> = emptyList()
    open suspend fun getPeriodById(id: Int): PeriodDbo? = null
    open suspend fun getSubjectById(id: Int): SubjectDbo? = null
    open suspend fun getSubjectsByPeriodId(id: Int): List<SubjectDbo> = emptyList()
    open suspend fun getLessonsByPeriodId(id: Int): List<LessonDbo> = emptyList()
    open suspend fun getLessonsBySubjectId(id: Int): List<LessonDbo> = emptyList()
    open suspend fun put(dbo: PeriodDbo) {}

    open class General : ScheduleApi() {
        open suspend fun getActiveLessons(): List<LessonDbo> = emptyList()
    }
    open class Persistent : ScheduleApi()
    open class External : ScheduleApi()

}