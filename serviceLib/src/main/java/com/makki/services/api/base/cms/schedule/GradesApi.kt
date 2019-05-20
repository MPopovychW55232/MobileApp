package com.makki.services.api.base.cms.schedule

import com.makki.basic.model.GradePeriodAsset
import org.koin.core.KoinComponent

/**
 * @author Maksym.Popovych
 */
abstract class GradesApi : KoinComponent{

    open suspend fun getAllGradePeriods(): List<GradePeriodAsset> = emptyList()

    open class General : GradesApi()
    open class Persistent : GradesApi()
    open class External : GradesApi()

}