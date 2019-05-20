package com.makki.languageapp.fragments.main.study

import androidx.lifecycle.ViewModel
import com.makki.languageapp.utils.SafeLiveData

/**
 * @author Maksym.Popovych
 */
class StudyScreenViewModel : ViewModel() {

    enum class Action{
        None, Grades, Announcement
    }

    val nextAction = SafeLiveData(Action.None)

    fun onGrades() {
        nextAction.postValue(Action.Grades)
    }

    fun onAnnouncements() {
        nextAction.postValue(Action.Announcement)
    }

}
