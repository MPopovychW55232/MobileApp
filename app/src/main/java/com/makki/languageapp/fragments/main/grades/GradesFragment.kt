package com.makki.languageapp.fragments.main.grades

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makki.languageapp.databinding.FragmentGradesBindingImpl
import com.makki.languageapp.lib.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author Maksym.Popovych
 */
class GradesFragment : BaseFragment() {

    companion object {
        fun newInstance() = GradesFragment()
    }

    private val model: GradesViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentGradesBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        return binding.root
    }

    override fun onBackPressed(): Boolean {
        return fragmentManager?.popBackStackImmediate() == true
    }

}
