package com.makki.languageapp.fragments.main.movement

import androidx.lifecycle.ViewModel
import com.makki.basic.model.LessonAsset
import com.makki.basic.model.MovementAsset
import com.makki.languageapp.fragments.main.schedule.ScheduleHelper
import com.makki.languageapp.lib.views.TimeGraphView
import com.makki.languageapp.utils.SafeLiveData
import com.makki.services.api.managers.MovementManager
import com.makki.services.api.managers.ScheduleManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.functions.BiFunction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit
import kotlin.math.min

/**
 * @author Maksym.Popovych
 */
class PersonViewModel : ViewModel(), KoinComponent {

    private val manager: MovementManager by inject()
    private val schedule: ScheduleManager by inject()
    private var disposable = Disposables.disposed()
    private var itemDisposable = Disposables.disposed()

    val movementList = SafeLiveData<List<TimeGraphView.Action>>(emptyList())
    val secondaryList = SafeLiveData<List<TimeGraphView.Action>>(emptyList())

    val selectedTitle = SafeLiveData("")
    val selectedId = SafeLiveData("")
    val adapter = SafeLiveData(MovementAdapter())
    val loading = SafeLiveData(true)

    init {
        disposable = manager.getAll().toList()
            .zipWith(schedule.getAllActive().toList(), object :
                BiFunction<List<MovementAsset>, List<LessonAsset>, Pair<List<MovementAsset>, List<LessonAsset>>> {
                override fun apply(
                    t: List<MovementAsset>,
                    u: List<LessonAsset>
                ): Pair<List<MovementAsset>, List<LessonAsset>> {
                    return Pair(t, u)
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { loading.postValue(false) }
            .subscribe { pair ->
                val actions = pair.first.map { it.toAction() }
                movementList.postValue(actions)

                val map = ScheduleHelper.processToDayActions(pair.second).associateBy { it.id }

                secondaryList.postValue(actions.map {
                    map[it.id] ?: return@map TimeGraphView.Action.Null
                })
            }
    }

    override fun onCleared() {
        super.onCleared()

        disposable.dispose()
        itemDisposable.dispose()
    }

    fun MovementAsset.toAction(): TimeGraphView.Action {
        if (this == MovementAsset.Null) return TimeGraphView.Action.Null

        val enterDate = DateTime(entered)
        val dayStart = enterDate.withTimeAtStartOfDay().millis
        val entered = (enterDate.millis - dayStart).toFloat() / TimeUnit.DAYS.toMillis(1) * 24f
        val left =
            (min(left, dayStart + TimeUnit.DAYS.toMillis(1) - 1) - dayStart).toFloat() / TimeUnit.DAYS.toMillis(1) * 24f

        return TimeGraphView.Action(dayStart.toString(), entered, left, DateTimeFormat.forPattern("\u2022 dd/MM").print(enterDate))
    }

    fun onDateSelected(id: String) {
        val timestamp = id.toLongOrNull() ?: return
        val startOfDay = DateTime(timestamp).withTimeAtStartOfDay()

        selectedId.postValue(id)

        itemDisposable.dispose()
        itemDisposable = manager.getByRange(startOfDay.millis, startOfDay.millis + TimeUnit.DAYS.toMillis(1))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { item ->
                if (item.entries.isEmpty()) return@subscribe

                selectedTitle.postValue("• Detailed data for: ${DateTimeFormat.forPattern("dd/MM").print(item.entered)}")
                adapter.value.setAll(item.entries)
            }
    }


}