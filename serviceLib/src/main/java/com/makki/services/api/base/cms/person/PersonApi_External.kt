package com.makki.services.api.base.cms.person

import android.net.Uri
import com.makki.basic.dbo.PersonDbo
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
class PersonApi_External : PersonApi.External() {

    private val client: HttpClient by inject()
    private val config: ApiConfiguration by inject()
    private val loginApi: LoginApi by inject()

    override suspend fun getById(id: Int): PersonDbo? =
        apiMethod {
            val url = Uri.parse(config.urlBase).buildUpon()
                .appendEncodedPath("person")
                .appendEncodedPath(id.toString())
                .appendQueryParameter("sid", loginApi.getSid())

            val request = Request.Builder()
                .url(url.toString())
                .build()

            val response = client.sendRequest(request, PersonResponse::class)
                    ?: throw ServiceException("Couldn't process request")

            if (!response.isValid()) {
                throw ServiceException(response.errorMessage, response.errorCode)
            }

            return response.user?.toDbo()
        }

    private data class PersonResponse(
        val user: PersonJson?
    ) : BaseResponse()

    private data class PersonJson(
        var id: Int = 0,
        var login: String = "",
        var name: String = "",
        var email: String = "",
        var city: String = "",
        var photo: String = "",
        var phone1: String = "",
        var phone2: String = "",
        var extra: String = "",
        var accessLevel: Int = 0
    ) {
        fun toDbo() : PersonDbo {
            return PersonDbo().also {
                it.id = id
                it.login = login
                it.name = name
                it.email = email
                it.city = city
                it.photo = photo
            }
        }
    }
}
