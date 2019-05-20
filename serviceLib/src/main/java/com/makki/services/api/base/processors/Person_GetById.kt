package com.makki.services.api.base.processors

import android.os.Bundle
import android.os.Parcelable
import com.makki.services.api.base.cms.person.PersonApi_General
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class Person_GetById : ParcelableProcessor() {

    val api: PersonApi_General by inject()

    override suspend fun doWork(bundle: Bundle): Parcelable? {
        val id = bundle.getInt("id")
        return api.getById(id)?.build()
    }

}