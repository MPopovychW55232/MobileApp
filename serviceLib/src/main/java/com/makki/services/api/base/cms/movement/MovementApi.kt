package com.makki.services.api.base.cms.movement

import com.makki.basic.dbo.MovementDbo
import org.koin.core.KoinComponent

/**
 * @author Maksym.Popovych
 */
abstract class MovementApi : KoinComponent {
    open suspend fun getAll(): List<MovementDbo> = emptyList()
    open suspend fun getByRange(start: Long, end: Long): List<MovementDbo> = emptyList()
    open suspend fun put(dbo: MovementDbo) {}

    open class General: MovementApi()
    open class Cache: MovementApi()
    open class Persistent: MovementApi()
    open class External: MovementApi()
}
