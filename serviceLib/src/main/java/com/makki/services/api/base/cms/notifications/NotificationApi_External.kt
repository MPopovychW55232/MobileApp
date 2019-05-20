package com.makki.services.api.base.cms.notifications

import android.net.Uri
import com.makki.basic.dbo.NotificationDbo
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

class NotificationApi_External : NotificationApi.External() {

    private val client: HttpClient by inject()
    private val config: ApiConfiguration by inject()
    private val loginApi: LoginApi by inject()

    override suspend fun getById(id: Int): NotificationDbo? =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("notifications")
                .appendEncodedPath(id.toString())
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, NotificationResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.notification?.toDbo()
        }

    override suspend fun getAll(): List<NotificationDbo> =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("notifications")
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, NotificationsResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.notifications.map { it.toDbo() }
        }

    private data class NotificationResponse(
        val notification: NotificationJson?
    ) : BaseResponse()

    private data class NotificationsResponse(
        val notifications: List<NotificationJson> = emptyList()
    ) : BaseResponse()

    private data class NotificationJson(
        var id: Int = 0,
        var receiverId: Int = 0,
        var senderId: Int = 0,
        var senderDisplayName: String = "",
        var title: String = "",
        var message: String = "",
        var fresh: Boolean = false,
        var timestamp: Long = 0
    ) {
        fun toDbo(): NotificationDbo {
            return NotificationDbo().also {
                it.id = id
                it.receiverId = receiverId
                it.senderId = senderId
                it.senderName = senderDisplayName
                it.title = title
                it.message = message
                it.fresh = fresh
                it.timestamp = timestamp
            }
        }
    }

}