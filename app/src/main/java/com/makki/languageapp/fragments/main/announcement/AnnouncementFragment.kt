package com.makki.languageapp.fragments.main.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makki.languageapp.databinding.FragmentAnnouncementBindingImpl
import com.makki.languageapp.lib.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author Maksym.Popovych
 */
class AnnouncementFragment : BaseFragment() {

    companion object {
        fun newInstance() = AnnouncementFragment()
    }

    private val model: AnnouncementViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAnnouncementBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        return binding.root
    }

    override fun onBackPressed(): Boolean {
        return fragmentManager?.popBackStackImmediate() == true
    }

}
