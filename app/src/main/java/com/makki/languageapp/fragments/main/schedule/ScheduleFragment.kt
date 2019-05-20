package com.makki.languageapp.fragments.main.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makki.languageapp.lib.base.BaseFragment
import com.makki.languageapp.databinding.FragmentScheduleBindingImpl
import org.koin.android.ext.android.inject

/**
 * @author Maksym.Popovych
 */

class ScheduleFragment: BaseFragment() {

    companion object {
        fun newInstance() = ScheduleFragment()
    }

    val model: ScheduleViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentScheduleBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        return binding.root
    }

}
