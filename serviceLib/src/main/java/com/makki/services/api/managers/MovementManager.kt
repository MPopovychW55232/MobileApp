package com.makki.services.api.managers

import android.os.Bundle
import com.makki.basic.injects.ProcessorsEnum
import com.makki.basic.model.MovementAsset
import com.makki.services.api.base.ManagerBase

/**
 * @author Maksym.Popovych
 */
class MovementManager : ManagerBase() {

    fun getAll() = sendAsObservable<MovementAsset>(ProcessorsEnum.MovementGetAll, Bundle())

    fun getByRange(start: Long, end: Long) = sendAsObservable<MovementAsset>(ProcessorsEnum.MovementGetByRange, Bundle().also {
        it.putLong("start", start)
        it.putLong("end", end)
    })

    fun getByDayId(id: String) = sendAsMaybe<MovementAsset>(ProcessorsEnum.MovementGetByDayId, Bundle().also {
        it.putString("id", id)
    })

}
