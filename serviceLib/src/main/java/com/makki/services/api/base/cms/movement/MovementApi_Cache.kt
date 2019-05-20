package com.makki.services.api.base.cms.movement

import com.makki.basic.dbo.MovementDbo

/**
 * @author Maksym.Popovych
 */
class MovementApi_Cache : MovementApi.Cache() {

    override suspend fun getAll(): List<MovementDbo> {
        return super.getAll()
    }

//    override suspend fun getById(id: String): MovementDbo? {
//        return super.getById(id)
//    }
}