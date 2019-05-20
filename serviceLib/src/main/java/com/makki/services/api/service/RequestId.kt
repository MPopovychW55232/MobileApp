package com.makki.services.api.service

import android.os.Messenger

/**
 * @author Maksym.Popovych
 */
class RequestId(val msg: Messenger, val id: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is RequestId) {
            return false
        }
        if (msg === other.msg) {
            return true
        }
        return msg == other.msg
    }
}