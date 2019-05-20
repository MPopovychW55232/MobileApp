package com.makki.services.api.scheduler


import io.reactivex.Scheduler
import kotlinx.coroutines.CoroutineDispatcher
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

/**
 * @author Maksym.Popovych
 */
class ExecutorDispatcher(
    private val name: String,
    private val executor: Executor
) : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) = executor.execute(block)
    override fun toString() = name

}

class SchedulerDispatcher(
    private val name: String,
    private val scheduler: Scheduler
) : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        scheduler.scheduleDirect(block)
    }

    override fun toString() = name
}
