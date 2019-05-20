package com.makki.services.api.base.cms.announcements

import android.net.Uri
import com.makki.basic.dbo.AnnouncementDbo
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

class AnnouncementApi_External : AnnouncementApi.External() {

    private val client: HttpClient by inject()
    private val config: ApiConfiguration by inject()
    private val loginApi: LoginApi by inject()

//    override suspend fun getById(id: Int): AnnouncementDbo? =
//        apiMethod {
//            val url = Uri.parse(config.urlBase).buildUpon()
//                .appendEncodedPath("announcements")
//                .appendEncodedPath(id.toString())
//                .appendQueryParameter("sid", loginApi.getSid())
//
//            val request = Request.Builder()
//                .url(url.toString())
//                .build()
//
//            val response = client.sendRequest(request, NotificationResponse::class)
//                ?: throw ServiceException("Couldn't process request")
//
//            if (!response.isValid()) {
//                throw ServiceException(response.errorMessage, response.errorCode)
//            }
//
//            return response.notification?.toDbo()
//        }

    override suspend fun getById(id: Int): AnnouncementDbo? =
        apiMethod {
            return@apiMethod getAll().find { it.id == id }
        }

    override suspend fun getAll(): List<AnnouncementDbo> =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("announcements")
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, AnnouncementsResponse::class)
                ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.announcements.map { it.toDbo() }
        }

    private data class AnnouncementsResponse(
        val announcements: List<AnnouncementJson> = emptyList()
    ) : BaseResponse()

    private data class AnnouncementJson(
        var id: Int = 0,
        var senderId: Int = 0,
        var senderDisplayName: String = "",
        var title: String = "",
        var message: String = "",
        var timestamp: Long = 0
    ) {
        fun toDbo(): AnnouncementDbo {
            return AnnouncementDbo().also {
                it.id = id
                it.senderId = senderId
                it.senderName = senderDisplayName
                it.title = title
                it.message = message
                it.timestamp = timestamp
            }
        }
    }

}