package com.makki.services.api.base.cms.schedule

import com.makki.basic.model.GradePeriodAsset
import com.makki.services.api.base.apiMethod
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class GradesApi_General : GradesApi.General() {

    private val external: GradesApi_External by inject()

    override suspend fun getAllGradePeriods(): List<GradePeriodAsset> =
            apiMethod {
                external.getAllGradePeriods()
            }

}
