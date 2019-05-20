package com.makki.languageapp.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.makki.languageapp.R
import com.makki.languageapp.databinding.FragmentMainBindingImpl
import com.makki.languageapp.fragments.main.notifications.NotificationFragment
import com.makki.languageapp.fragments.main.movement.PersonScreenFragment
import com.makki.languageapp.fragments.main.schedule.ScheduleFragment
import com.makki.languageapp.fragments.main.study.StudyScreenFragment
import com.makki.languageapp.lib.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author Maksym.Popovych
 */
class MainFragment : BaseFragment(), Observer<MainFragViewModel.Action> {

    companion object {
        fun newInstance() = MainFragment()
    }

    val model by viewModel<MainFragViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.selectedAction.observe(this, this)
    }

    override fun onChanged(step: MainFragViewModel.Action) {
        val manager = childFragmentManager
        var nextFragment = manager.findFragmentByTag(step.name)

        nextFragment = nextFragment ?: when (step) {
            MainFragViewModel.Action.Study -> {
                StudyScreenFragment.newInstance()
            }
            MainFragViewModel.Action.Notifications -> {
                NotificationFragment.newInstance()
            }
            MainFragViewModel.Action.StudentStatus -> {
                PersonScreenFragment.newInstance()
            }
            MainFragViewModel.Action.Schedule -> {
                ScheduleFragment.newInstance()
            }
            MainFragViewModel.Action.Close -> {
                activity?.onBackPressed()
                return
            }
        }

        manager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.screen_container, nextFragment, step.name)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed(): Boolean {
        return if (model.selectedAction.value == MainFragViewModel.Action.Close) {
            false
        } else {
            model.popTab()
            true
        }
    }
}
