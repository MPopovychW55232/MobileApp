package com.makki.services.api.managers

import android.os.Bundle
import com.makki.basic.injects.ProcessorsEnum
import com.makki.basic.model.PersonAsset
import com.makki.services.api.base.ManagerBase

/**
 * @author Maksym.Popovych
 */

class PersonManager : ManagerBase() {

    fun getById(id: Int) = sendAsMaybe<PersonAsset>(ProcessorsEnum.PersonGetById, Bundle().also {
        it.putInt("id", id)
    })

}
