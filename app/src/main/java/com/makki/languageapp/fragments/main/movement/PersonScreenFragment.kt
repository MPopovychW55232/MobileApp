package com.makki.languageapp.fragments.main.movement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makki.languageapp.databinding.FragmentPersonScreenBindingImpl
import com.makki.languageapp.lib.base.BaseFragment
import org.koin.android.ext.android.inject

/**
 * @author Maksym.Popovych
 */

class PersonScreenFragment : BaseFragment() {

    companion object {
        fun newInstance() = PersonScreenFragment()
    }

    val model: PersonViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentPersonScreenBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        return binding.root
    }

}