package com.makki.services.api.base.cms.base

import org.koin.core.KoinComponent
import java.util.concurrent.TimeUnit

/**
 * @author Maksym.Popovych
 */
open class ApiConfiguration : KoinComponent {

    open val urlBase: String = "http://192.168.1.9:8080/api"
    open val timeout: Long = TimeUnit.SECONDS.toMillis(5)

}
