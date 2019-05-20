package com.makki.services.api.base.cms.base

import android.util.Log
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * @author Maksym.Popovych
 */

class HttpClient : KoinComponent {

    private val config: ApiConfiguration by inject()

    private val client = OkHttpClient.Builder()
        .connectTimeout(config.timeout, TimeUnit.MILLISECONDS)
        .readTimeout(config.timeout, TimeUnit.MILLISECONDS)
        .writeTimeout(config.timeout, TimeUnit.MILLISECONDS)
        .addInterceptor(RequestLog())
        .build()

    private val mapper = ObjectMapper().also {
        it.registerKotlinModule()
        it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    fun <T: Any> sendRequest(request: Request, clazz: KClass<T>): T? {
        return sendRequest(request, clazz.java)
    }

    fun <T : Any> sendRequest(request: Request, clazz: Class<T>): T? {
        val response = client.newCall(request).execute()
        val stream = response.body()?.byteStream()
        return mapper.readValue(stream, clazz)
    }

    inner class RequestLog : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            Log.i("eSchool", request.url().toString())
            return chain.proceed(request)
        }
    }
}



















