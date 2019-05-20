package com.makki.languageapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makki.languageapp.databinding.FragmentSignUpBindingImpl
import com.makki.languageapp.lib.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
/**
 * @author Maksym.Popovych
 */
class SignUpFragment : BaseFragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    val model by viewModel<SignUpViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSignUpBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model
        return binding.root
    }

}
