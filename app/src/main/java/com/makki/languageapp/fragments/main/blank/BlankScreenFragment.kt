package com.makki.languageapp.fragments.main.blank

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makki.languageapp.databinding.FragmentBlankScreenBindingImpl
import com.makki.languageapp.lib.base.BaseFragment

/**
 * @author Maksym.Popovych
 */

class BlankScreenFragment : BaseFragment() {

    companion object {
        fun newInstance() = BlankScreenFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentBlankScreenBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
//        binding.model = model

        return binding.root
    }

}