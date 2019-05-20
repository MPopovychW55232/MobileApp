package com.makki.services.api.base.cms.login

import android.net.Uri
import com.makki.basic.model.LoginResponse
import com.makki.basic.model.ServiceException
import com.makki.services.api.base.apiMethod
import com.makki.services.api.base.cms.base.ApiConfiguration
import com.makki.services.api.base.cms.base.BaseResponse
import com.makki.services.api.base.cms.base.HttpClient
import okhttp3.Request
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class LoginApi : KoinComponent {

    private val client: HttpClient by inject()
    private val config: ApiConfiguration by inject()

    private var sessionId: String = ""

    suspend fun login(name: String, password: String): LoginResponse =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("login")
                .appendQueryParameter("login", name)
                .appendQueryParameter("password", password)

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response =
                client.sendRequest(request, Session::class) ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            sessionId = response.sid

            return LoginResponse("Logged in with: $name", true)
        }

    suspend fun getSid() = sessionId

    private data class Session(
        val sid: String = ""
    ) : BaseResponse()

}