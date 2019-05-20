package com.makki.services.api.base.cms.schedule

import android.net.Uri
import com.makki.basic.model.GradeAsset
import com.makki.basic.model.GradePeriodAsset
import com.makki.basic.model.ServiceException
import com.makki.languageapp.lib.utils.merge
import com.makki.services.api.base.apiAsync
import com.makki.services.api.base.apiMethod
import com.makki.services.api.base.cms.base.ApiConfiguration
import com.makki.services.api.base.cms.base.BaseResponse
import com.makki.services.api.base.cms.base.HttpClient
import com.makki.services.api.base.cms.login.LoginApi
import kotlinx.coroutines.awaitAll
import okhttp3.Request
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class GradesApi_External : GradesApi.External() {

    private val client: HttpClient by inject()
    private val config: ApiConfiguration by inject()
    private val loginApi: LoginApi by inject()

    private val scheduleApi: ScheduleApi_General by inject()

    override suspend fun getAllGradePeriods(): List<GradePeriodAsset> =
        apiMethod {
            val periods = scheduleApi.getAllPeriods()

            val subjects = periods.map { apiAsync { scheduleApi.getSubjectsByPeriodId(it.id) } }.awaitAll().merge()
            val grades = periods.map { apiAsync { getGradesForPeriod(it.id) } }.awaitAll().merge()

            val gradesMap = grades.groupBy { it.subjectId }
            val subjectMap = subjects.groupBy { it.periodId }

            return@apiMethod periods.map { period ->
                val gradeTable = subjectMap[period.id]?.map { subject ->
                    val lastGrade = gradesMap[subject.id]?.maxBy { it.timestamp }
                    GradeAsset(
                        subject.id,
                        period.id,
                        subject.id,
                        subject.title,
                        lastGrade?.grade ?: "",
                        lastGrade?.notices ?: "",
                        lastGrade?.timestamp ?: 0L
                    )
                } ?: emptyList()
                GradePeriodAsset(
                    period.id,
                    period.title,
                    period.startTime,
                    period.endTime,
                    period.modified,
                    gradeTable
                )
            }
        }

    private suspend fun getGradesForPeriod(periodId: Int): List<Grade> =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("grades")
                .appendEncodedPath("period")
                .appendQueryParameter("period_id", periodId.toString())
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, GradeResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.grades
        }

    private data class GradeResponse(
        val grades: List<Grade>
    ) : BaseResponse()

    private data class Grade(
        val id: Int,
        val personId: Int,
        val periodId: Int,
        val subjectId: Int,
        val timestamp: Long,
        val grade: String,
        val notices: String
    )
}
