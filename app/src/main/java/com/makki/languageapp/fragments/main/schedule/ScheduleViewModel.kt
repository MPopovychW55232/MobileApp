package com.makki.languageapp.fragments.main.schedule

import androidx.lifecycle.ViewModel
import com.makki.basic.model.LessonAsset
import com.makki.languageapp.lib.views.TimeGraphView
import com.makki.languageapp.utils.SafeLiveData
import com.makki.services.api.managers.ScheduleManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit

/**
 * @author Maksym.Popovych
 */
class ScheduleViewModel : ViewModel(), KoinComponent {

    val manager: ScheduleManager by inject()

    var lessons: List<LessonAsset> = emptyList()
    val scheduleList = SafeLiveData<List<TimeGraphView.Action>>(emptyList())
    val selectedTitle = SafeLiveData("")
    val selectedId = SafeLiveData("")
    val adapter = SafeLiveData(ScheduleAdapter())
    val loading = SafeLiveData(true)

    private var disposable = Disposables.disposed()

    init {
        disposable = manager.getAllActive()
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
            .doFinally { loading.postValue(false) }
            .subscribe { all ->
                lessons = all
                scheduleList.postValue(ScheduleHelper.processToDayActions(lessons))
            }
    }

    fun onDateSelected(id: String) {
        val timestamp = id.toLongOrNull() ?: return
        val startOfDay = DateTime(timestamp).withTimeAtStartOfDay()

        selectedId.postValue(id)
        val start = startOfDay.millis
        val end = startOfDay.millis + TimeUnit.DAYS.toMillis(1)
        val detailed = lessons.filter { start < it.start && (it.start + it.duration) < end }

        adapter.value.setAll(detailed)

        val selectedTime = detailed.firstOrNull()?.start ?: return
        selectedTitle.postValue("• Detailed data for: ${DateTimeFormat.forPattern("dd/MM").print(selectedTime)}")
    }

}

