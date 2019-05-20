package com.makki.services.api.base.cms.movement

import android.net.Uri
import com.makki.basic.dbo.MovementDbo
import com.makki.basic.dbo.MovementRecordDbo
import com.makki.basic.model.ServiceException
import com.makki.services.api.base.apiMethod
import com.makki.services.api.base.cms.base.ApiConfiguration
import com.makki.services.api.base.cms.base.BaseResponse
import com.makki.services.api.base.cms.base.HttpClient
import com.makki.services.api.base.cms.login.LoginApi
import okhttp3.Request
import org.joda.time.format.DateTimeFormat
import org.koin.core.inject
import java.util.concurrent.TimeUnit

/**
 * @author Maksym.Popovych
 */
class MovementApi_External : MovementApi.External() {

    private val client: HttpClient by inject()
    private val config: ApiConfiguration by inject()
    private val loginApi: LoginApi by inject()

    override suspend fun getAll(): List<MovementDbo> {
        val now = System.currentTimeMillis()
        return getByRange(now - TimeUnit.DAYS.toMillis(183), now + TimeUnit.HOURS.toMillis(1))
    }

    override suspend fun getByRange(start: Long, end: Long): List<MovementDbo> =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("movement")
                .appendQueryParameter("start", start.toString())
                .appendQueryParameter("end", end.toString())
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, MovementResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return processData(response.movement)
        }

    private fun processData(list: List<Movement>): List<MovementDbo> {
        if (list.isEmpty()) return emptyList()

        val formatter = DateTimeFormat.forPattern("dd/MM")

        val map = list.groupBy { item -> formatter.print(item.timestamp) }
        var start = list.minBy { it.timestamp }?.timestamp ?: return emptyList()

        var safeIndex = 0
        val result = ArrayList<MovementDbo>()
        while (safeIndex < 400 && start <= System.currentTimeMillis()) {
            val key = formatter.print(start)
            if (map[key] == null) {
                result.add(MovementDbo.Null)
            } else {
                val records = map[key] ?: continue
                val dbo = MovementDbo()
                dbo.id = records.minBy { it.timestamp }?.timestamp.toString()
                dbo.entries = records.map { record ->
                    MovementRecordDbo().also {
                        it.id = record.id
                        it.entered = record.entered
                        it.gate = record.gate
                        it.parentDayId = key
                        it.dayTag = key
                        it.timestamp = record.timestamp
                    }
                }
                result.add(dbo)
            }

            start += TimeUnit.DAYS.toMillis(1)
            safeIndex++
        }

        return result.asReversed()
    }

    private data class MovementResponse(
        val movement: List<Movement> = emptyList()
    ) : BaseResponse()

    private data class Movement(
        val id: Int,
        val personId: Int,
        val gate: String,
        val monthId: Int,
        val dayId: Int,
        val timestamp: Long,
        val entered: Boolean
    ) : BaseResponse()
}