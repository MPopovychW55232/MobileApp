package com.makki.services.api.base.cms.person

import com.makki.basic.dbo.PersonDbo
import org.koin.core.KoinComponent

/**
 * @author Maksym.Popovych
 */
abstract class PersonApi : KoinComponent {

    open suspend fun getById(id: Int): PersonDbo? = null
    open suspend fun put(dbo: PersonDbo) {}

    open class General : PersonApi()
    open class Persistent : PersonApi()
    open class External : PersonApi()

}