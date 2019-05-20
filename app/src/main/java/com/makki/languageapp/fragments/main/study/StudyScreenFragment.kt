package com.makki.languageapp.fragments.main.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.makki.languageapp.R
import com.makki.languageapp.databinding.FragmentStudyScreenBindingImpl
import com.makki.languageapp.fragments.main.announcement.AnnouncementFragment
import com.makki.languageapp.fragments.main.grades.GradesFragment
import com.makki.languageapp.lib.base.BaseFragment
import org.koin.android.ext.android.inject

/**
 * @author Maksym.Popovych
 */

class StudyScreenFragment : BaseFragment(), Observer<StudyScreenViewModel.Action> {
    companion object {
        fun newInstance() = StudyScreenFragment()
    }

    val model: StudyScreenViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentStudyScreenBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.nextAction.observe(this, this)
    }

    override fun onChanged(t: StudyScreenViewModel.Action) {
        when(t) {
            StudyScreenViewModel.Action.Grades -> onGrades()
            StudyScreenViewModel.Action.Announcement -> onAnnouncements()
            else -> return
        }
    }

    private fun onGrades() {
        val manager = fragmentManager ?: return
        manager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(id, GradesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun onAnnouncements() {
        val manager = fragmentManager ?: return
        manager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(id, AnnouncementFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

}