package com.makki.services.api.managers

import android.os.Bundle
import com.makki.basic.injects.ProcessorsEnum
import com.makki.basic.model.NotificationAsset
import com.makki.services.api.base.ManagerBase

/**
 * @author Maksym.Popovych
 */
class NotificationManager : ManagerBase() {

    fun getAll() = sendAsObservable<NotificationAsset>(ProcessorsEnum.NotificationGetAll, Bundle())

}
