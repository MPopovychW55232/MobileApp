package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.basic.model.ServiceException
import com.makki.services.api.base.cms.login.LoginApi
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class LoginProcessor : ParcelableProcessor() {

    val api: LoginApi by inject()

    override suspend fun doWork(bundle: Bundle): Parcelable? {
        val name = bundle.getString("name") ?: throw ServiceException("Error - missing login")
        val password = bundle.getString("password") ?: throw ServiceException("Error - missing password")
        return api.login(name, password)
    }
}