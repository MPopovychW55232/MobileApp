package com.makki.services.api.managers

import android.os.Bundle
import com.makki.basic.injects.ProcessorsEnum
import com.makki.basic.model.AnnouncementAsset
import com.makki.basic.model.NotificationAsset
import com.makki.services.api.base.ManagerBase

/**
 * @author Maksym.Popovych
 */
class AnnouncementManager : ManagerBase() {

    fun getAll() = sendAsObservable<AnnouncementAsset>(ProcessorsEnum.AnnouncementGetAll, Bundle())

}
