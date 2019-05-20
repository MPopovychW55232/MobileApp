package com.makki.languageapp.fragments.main

import androidx.lifecycle.ViewModel
import com.makki.languageapp.utils.StackLiveData
import com.makki.languageapp.utils.SafeLiveData

/**
 * @author Maksym.Popovych
 */
class MainFragViewModel : ViewModel() {

    enum class Action { Study, Notifications, StudentStatus, Schedule, Close }

    val studyActive = SafeLiveData(true)
    val notificationsActive = SafeLiveData(false)
    val studentStatusActive = SafeLiveData(false)
    val scheduleActive = SafeLiveData(false)

    val selectedAction = StackLiveData(Action.Study, Action.Close)

    fun onStudy() {
        if (studyActive.value) return

        studyActive.postValue(true)
        notificationsActive.postValue(false)
        studentStatusActive.postValue(false)
        scheduleActive.postValue(false)

        selectedAction.postValue(Action.Study)
    }

    fun onNotifications() {
        if (notificationsActive.value) return

        studyActive.postValue(false)
        notificationsActive.postValue(true)
        studentStatusActive.postValue(false)
        scheduleActive.postValue(false)

        selectedAction.postValue(Action.Notifications)
    }

    fun onStudentStatus() {
        if (studentStatusActive.value) return

        studyActive.postValue(false)
        notificationsActive.postValue(false)
        studentStatusActive.postValue(true)
        scheduleActive.postValue(false)

        selectedAction.postValue(Action.StudentStatus)
    }

    fun onSchedule() {
        if (scheduleActive.value) return

        studyActive.postValue(false)
        notificationsActive.postValue(false)
        studentStatusActive.postValue(false)
        scheduleActive.postValue(true)

        selectedAction.postValue(Action.Schedule)
    }

    fun popTab() {
        selectedAction.popValue {
            when(it) {
                Action.Study -> { onStudy() }
                Action.Notifications -> { onNotifications() }
                Action.StudentStatus -> { onStudentStatus() }
                Action.Schedule -> { onSchedule() }
                Action.Close -> { selectedAction.postValue(Action.Close)}
            }
        }
    }

}
