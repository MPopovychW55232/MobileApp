package com.makki.services.api.base.database

import android.app.Application
import androidx.room.Room
import com.makki.services.api.scheduler.ExecutorDispatcher
import com.makki.services.api.scheduler.SchedulerDispatcher
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import java.util.concurrent.Executors

/**
 * @author Maksym.Popovych
 */
class DatabaseComponent(application: Application) : KoinComponent {

    companion object {
        val readThreadDispatcher = SchedulerDispatcher("DB_ReadThread", Schedulers.io())
        val writeThreadDispatcher = ExecutorDispatcher("DB_WriteThread", Executors.newSingleThreadExecutor())
    }

    private val instance by lazy {
        Room.databaseBuilder(application, DatabaseBase::class.java, "database.sqlite")
            .fallbackToDestructiveMigration()
            .build()
    }

    suspend fun <T> readTransaction(fallback: (() -> T)? = null, block: DatabaseCallback<T>) =
        transaction(readThreadDispatcher, fallback = fallback, block = block)

    suspend fun <T> writeTransaction(fallback: (() -> T)? = null, block: DatabaseCallback<T>) =
        transaction(writeThreadDispatcher, fallback = fallback, block = block)

    private suspend fun <T> transaction(
        dispatcher: CoroutineDispatcher,
        fallback: (() -> T)? = null,
        block: DatabaseCallback<T>): T {

        return withContext(dispatcher) {
            async {
                return@async block(instance)
            }.await()
        }
    }
}

typealias DatabaseCallback<T> = suspend (dao: DatabaseBase) -> T