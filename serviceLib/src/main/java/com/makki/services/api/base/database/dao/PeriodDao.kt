package com.makki.services.api.base.database.dao

import androidx.room.Dao
import com.makki.basic.dbo.PeriodDbo
import com.makki.services.api.base.database.DaoBase

/**
 * @author Maksym.Popovych
 */
@Dao
abstract class PeriodDao : DaoBase<PeriodDbo>(PeriodDbo::class.java) {



}