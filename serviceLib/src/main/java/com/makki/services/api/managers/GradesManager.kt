package com.makki.services.api.managers

import android.os.Bundle
import com.makki.basic.injects.ProcessorsEnum
import com.makki.basic.model.GradePeriodAsset
import com.makki.services.api.base.ManagerBase

/**
 * @author Maksym.Popovych
 */
class GradesManager : ManagerBase() {

    fun getAll() = sendAsObservable<GradePeriodAsset>(ProcessorsEnum.GradesGetAll, Bundle())

}