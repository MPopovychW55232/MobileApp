package com.makki.services.api.base.cms.base

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Maksym.Popovych
 */
open class BaseResponse {

    @JsonProperty("error_code")
    val errorCode: Int = 0
    @JsonProperty("error_message")
    val errorMessage: String = ""

    fun isValid() = errorCode == 0

}