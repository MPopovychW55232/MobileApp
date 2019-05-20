package com.makki.services.api.base.cms.schedule

import android.net.Uri
import com.makki.basic.dbo.LessonDbo
import com.makki.basic.dbo.PeriodDbo
import com.makki.basic.dbo.SubjectDbo
import com.makki.basic.model.ServiceException
import com.makki.services.api.base.apiMethod
import com.makki.services.api.base.cms.base.ApiConfiguration
import com.makki.services.api.base.cms.base.BaseResponse
import com.makki.services.api.base.cms.base.HttpClient
import com.makki.services.api.base.cms.login.LoginApi
import okhttp3.Request
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */

class ScheduleApi_External : ScheduleApi.External() {

    private val client: HttpClient by inject()
    private val config: ApiConfiguration by inject()
    private val loginApi: LoginApi by inject()

    private val generalApi: ScheduleApi_General by inject()

    override suspend fun getAllPeriods(): List<PeriodDbo> =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("schedule")
                .appendEncodedPath("periods")
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, PeriodsResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.periods.map { it.toDbo() }
        }

    override suspend fun getActivePeriods(): List<PeriodDbo> =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("schedule")
                .appendEncodedPath("periods")
                .appendEncodedPath("active")
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, PeriodsResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.periods.map { it.toDbo() }
        }

    override suspend fun getPeriodById(id: Int): PeriodDbo? =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("schedule")
                .appendEncodedPath("periods")
                .appendEncodedPath(id.toString())
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, PeriodResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.period?.toDbo()
        }

    override suspend fun getSubjectById(id: Int): SubjectDbo? =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("schedule")
                .appendEncodedPath("subjects")
                .appendEncodedPath(id.toString())
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, SubjectResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.subject?.toDbo()
        }

    override suspend fun getSubjectsByPeriodId(id: Int): List<SubjectDbo> =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("schedule")
                .appendEncodedPath("subjects")
                .appendQueryParameter("period_id", id.toString())
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, SubjectsResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.subjects.map { it.toDbo() }
        }

    override suspend fun getLessonsByPeriodId(id: Int): List<LessonDbo> =
        apiMethod {
            val period = generalApi.getPeriodById(id) ?: return emptyList()

            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("schedule")
                .appendEncodedPath("lessons")
                .appendQueryParameter("period_id", id.toString())
                .appendQueryParameter("start", period.startTime.toString())
                .appendQueryParameter("end", period.endTime.toString())
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, LessonsResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.lessons.map { it.toDbo() }
        }

    override suspend fun getLessonsBySubjectId(id: Int): List<LessonDbo> =
        apiMethod {
            val subject = generalApi.getSubjectById(id) ?: return emptyList()
            val period = generalApi.getPeriodById(id) ?: return emptyList()

            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("schedule")
                .appendEncodedPath("lessons")
                .appendQueryParameter("period_id", subject.periodId.toString())
                .appendQueryParameter("start", period.startTime.toString())
                .appendQueryParameter("end", period.endTime.toString())
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, LessonsResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.lessons.map {
                it.toDbo()
            }
        }

    data class PeriodsResponse(
        var periods: List<Period> = emptyList()
    ) : BaseResponse()

    data class PeriodResponse(
        var period: Period? = null
    ) : BaseResponse()

    data class Period(
        val id: Int,
        val startTime: Long,
        val endTime: Long,
        val name: String,
        val description: String,
        val modified: Long = 0
    ) {
        fun toDbo(): PeriodDbo {
            return PeriodDbo().also {
                it.id = id
                it.startTime = startTime
                it.endTime = endTime
                it.title = name
                it.description = name
                it.modified = modified
            }
        }
    }

    data class SubjectsResponse(
        val subjects: List<Subject> = emptyList()
    ) : BaseResponse()

    data class SubjectResponse(
        val subject: Subject? = null
    ) : BaseResponse()

    data class Subject(
        val id: Int,
        val periodId: Int,
        val name: String,
        val description: String
    ) {
        fun toDbo(): SubjectDbo {
            return SubjectDbo().also {
                it.id = id
                it.periodId = periodId
                it.title = name
                it.description = description
            }
        }
    }

    data class LessonsResponse(
        val lessons: List<Lesson> = emptyList()
    ) : BaseResponse()

    data class Lesson(
        val id: Int,
        val subjectId: Int,
        val subjectName: String,
        val periodId: Int,
        val monthId: Int,
        val start: Long,
        val duration: Long,
        val extra: String
    ) {
        fun toDbo(): LessonDbo {
            return LessonDbo().also {
                it.id
                it.subjectId = subjectId
                it.subjectName = subjectName
                it.periodId = periodId
                it.monthId = monthId
                it.startTime = start
                it.duration = duration
                it.extra = extra
            }
        }
    }

}