package com.makki.services.api.managers

import android.os.Bundle
import com.makki.services.api.base.ManagerBase
import com.makki.basic.injects.ProcessorsEnum
import com.makki.basic.model.LoginResponse

/**
 * @author Maksym.Popovych
 */
class AuthManager : ManagerBase() {

    fun login(name: String, password: String) = sendAsSingle<LoginResponse>(ProcessorsEnum.Login, Bundle().also {
        it.putString("name", name)
        it.putString("password", password)
    })

}
