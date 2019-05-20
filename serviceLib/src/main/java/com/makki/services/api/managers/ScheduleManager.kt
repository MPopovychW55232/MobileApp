package com.makki.services.api.managers

import android.os.Bundle
import com.makki.basic.injects.ProcessorsEnum
import com.makki.basic.model.LessonAsset
import com.makki.services.api.base.ManagerBase

/**
 * @author Maksym.Popovych
 */

class ScheduleManager : ManagerBase() {

    fun getAllActive() = sendAsObservable<LessonAsset>(ProcessorsEnum.ScheduleGetAllActive, Bundle())

    fun getByRange(start: Long, end: Long) = getAllActive().filter { start < it.start && (it.start + it.duration) < end }

}